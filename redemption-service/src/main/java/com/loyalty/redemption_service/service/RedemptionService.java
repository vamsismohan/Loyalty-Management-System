package com.loyalty.redemption_service.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.redemption_service.dto.PointsRedeemedEvent;
import com.loyalty.redemption_service.dto.RedemptionRequestDTO;
import com.loyalty.redemption_service.dto.RedemptionResponseDTO;
import com.loyalty.redemption_service.entity.RedemptionTransaction;
import com.loyalty.redemption_service.feign.PartnerClient;
import com.loyalty.redemption_service.feign.dto.PartnerRedemptionRuleDTO;
import com.loyalty.redemption_service.repository.RedemptionTransactionRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedemptionService {

    private final RedemptionTransactionRepository repo;
    private final PartnerClient partnerClient;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "points.redeemed";

    @Transactional
    public RedemptionResponseDTO processRedemption(RedemptionRequestDTO request) {
        RedemptionTransaction tx = new RedemptionTransaction();
        tx.setMembershipNumber(request.getMembershipNumber());
        tx.setPartner(request.getPartner());
        tx.setPointType(request.getPointType());
        tx.setPointsUsed(request.getPointsRequested());
        tx.setRewardType(request.getRewardType());
        tx.setReferenceId(request.getReferenceId());
        tx.setStatus("PENDING");

        tx = repo.save(tx);

        PartnerRedemptionRuleDTO rule = fetchRedemptionRule(request.getPartner(), request.getPointType());

        // validate against rule
        boolean ok = rule != null && rule.getActive() != null && rule.getActive() && rule.getPointsRequired() <= request.getPointsRequested();
        tx.setStatus(ok ? "PUBLISHED" : "REJECTED");
        repo.save(tx);

        if (ok) {
            PointsRedeemedEvent event = new PointsRedeemedEvent();
            event.setMembershipNumber(request.getMembershipNumber());
            event.setPartner(request.getPartner());
            event.setPointType(request.getPointType());
            event.setPoints(request.getPointsRequested());
            event.setReferenceId(request.getReferenceId());
            event.setRewardType(request.getRewardType());
            try {
                String payload = objectMapper.writeValueAsString(event);
                kafkaTemplate.send(TOPIC, request.getMembershipNumber(), payload);
            } catch (Exception ex) {
                // keep PUBLISHED status; infra will handle delivery
            }
        }

        RedemptionResponseDTO resp = new RedemptionResponseDTO();
        resp.setTransactionId(tx.getId());
        resp.setMembershipNumber(tx.getMembershipNumber());
        resp.setStatus(tx.getStatus());
        resp.setPointsUsed(tx.getPointsUsed());
        return resp;
    }

    @CircuitBreaker(name = "partnerService", fallbackMethod = "partnerRuleFallback")
    public PartnerRedemptionRuleDTO fetchRedemptionRule(String partner, String pointType) {
        PartnerRedemptionRuleDTO rule = partnerClient.getRedemptionRule(partner, pointType);
        if (rule != null && rule.getActive() != null && rule.getActive()) {
            String key = "partner:redemption:" + partner + ":" + pointType;
            redisTemplate.opsForValue().set(key, String.valueOf(rule.getPointsRequired()));
        }
        return rule;
    }

    public PartnerRedemptionRuleDTO partnerRuleFallback(String partner, String pointType, Throwable ex) {
        String key = "partner:redemption:" + partner + ":" + pointType;
        String cached = redisTemplate.opsForValue().get(key);
        PartnerRedemptionRuleDTO rule = new PartnerRedemptionRuleDTO();
        rule.setPartner(partner);
        rule.setPointType(pointType);
        if (cached != null) {
            rule.setPointsRequired(Long.parseLong(cached));
            rule.setActive(true);
        } else {
            rule.setPointsRequired(Long.MAX_VALUE);
            rule.setActive(false);
        }
        return rule;
    }
}
