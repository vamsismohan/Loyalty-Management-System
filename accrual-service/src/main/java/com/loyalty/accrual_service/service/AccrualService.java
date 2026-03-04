package com.loyalty.accrual_service.service;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.accrual_service.dto.AccrualRequestDTO;
import com.loyalty.accrual_service.dto.AccrualResponseDTO;
import com.loyalty.accrual_service.dto.PointsAccruedEvent;
import com.loyalty.accrual_service.entity.AccrualTransaction;
import com.loyalty.accrual_service.feign.PartnerClient;
import com.loyalty.accrual_service.feign.dto.PartnerAccrualRuleDTO;
import com.loyalty.accrual_service.repository.AccrualTransactionRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccrualService {

    private final AccrualTransactionRepository repo;
    private final PartnerClient partnerClient;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "points.accrued";

    @Transactional
    public AccrualResponseDTO processAccrual(AccrualRequestDTO request) {

        // 1. Persist transaction as PENDING
        AccrualTransaction tx = new AccrualTransaction();
        tx.setMembershipNumber(request.getMembershipNumber());
        tx.setPartner(request.getPartner());
        tx.setPointType(request.getPointType());
        tx.setActivityType(request.getActivityType());
        tx.setActivityValue(request.getActivityValue());
        tx.setCabinClass(request.getCabinClass());
        tx.setReferenceId(request.getReferenceId());
        tx.setStatus("PENDING");

        tx = repo.save(tx);

        // 2. Fetch partner rule (with circuit breaker)
        PartnerAccrualRuleDTO rule = fetchPartnerRule(request.getPartner(), request.getPointType());

        long points = calculatePoints(request, rule);

        tx.setPointsEarned(points);
        tx.setStatus("PUBLISHED");
        repo.save(tx);

        // 3. Publish event
        PointsAccruedEvent event = new PointsAccruedEvent();
        event.setMembershipNumber(request.getMembershipNumber());
        event.setPartner(request.getPartner());
        event.setPointType(request.getPointType());
        event.setPoints(points);
        event.setReferenceId(request.getReferenceId());
        event.setActivityType(request.getActivityType());
        event.setActivityValue(request.getActivityValue());
        event.setCabinClass(request.getCabinClass());

        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, request.getMembershipNumber(), payload);
        } catch (Exception ex) {
            // on kafka failure, keep record PUBLISHED (will retry via infra)
        }

        AccrualResponseDTO resp = new AccrualResponseDTO();
        resp.setTransactionId(tx.getId());
        resp.setMembershipNumber(tx.getMembershipNumber());
        resp.setStatus(tx.getStatus());
        resp.setPointsEarned(points);
        return resp;
    }

    @CircuitBreaker(name = "partnerService", fallbackMethod = "partnerRuleFallback")
    public PartnerAccrualRuleDTO fetchPartnerRule(String partner, String pointType) {
        PartnerAccrualRuleDTO rule = partnerClient.getAccrualRule(partner, pointType);
        // cache for future
        if (rule != null && rule.getActive() != null && rule.getActive()) {
            String key = "partner:accrual:" + partner + ":" + pointType;
            redisTemplate.opsForValue().set(key, String.valueOf(rule.getPointsPerUnit()));
        }
        return rule;
    }

    public PartnerAccrualRuleDTO partnerRuleFallback(String partner, String pointType, Throwable ex) {
        // try cache
        String key = "partner:accrual:" + partner + ":" + pointType;
        String cached = redisTemplate.opsForValue().get(key);
        PartnerAccrualRuleDTO rule = new PartnerAccrualRuleDTO();
        rule.setPartner(partner);
        rule.setPointType(pointType);
        if (cached != null) {
            rule.setPointsPerUnit(Double.parseDouble(cached));
            rule.setActive(true);
        } else {
            // default conservative rule
            rule.setPointsPerUnit(0.0);
            rule.setActive(false);
        }
        return rule;
    }

    private long calculatePoints(AccrualRequestDTO req, PartnerAccrualRuleDTO rule) {
        if (rule == null || rule.getPointsPerUnit() == null) return 0L;

        double base = rule.getPointsPerUnit();
        double multiplier = 1.0;
        if ("BUSINESS".equalsIgnoreCase(req.getCabinClass())) multiplier = 1.5;
        else if ("PREMIUM".equalsIgnoreCase(req.getCabinClass())) multiplier = 1.25;

        // if unitType is DISTANCE, activityValue is distance
        double raw = base * req.getActivityValue() * multiplier;
        return Math.round(raw);
    }
}
