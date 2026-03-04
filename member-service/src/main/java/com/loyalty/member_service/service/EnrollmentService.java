package com.loyalty.member_service.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.loyalty.member_service.dto.CustomerDto;
import com.loyalty.member_service.dto.CustomerRequest;
import com.loyalty.member_service.dto.MemberResponse;
import com.loyalty.member_service.dto.PointMasterRequestDTO;
import com.loyalty.member_service.dto.PointMasterResponseDTO;
import com.loyalty.member_service.entity.Member;
import com.loyalty.member_service.entity.PointMaster;
import com.loyalty.member_service.exception.CustomerServerSideError;
import com.loyalty.member_service.exception.CustomerServiceUnavailableException;
import com.loyalty.member_service.exception.MemberAlreadyExistsException;
import com.loyalty.member_service.exception.PointTypeExistException;
import com.loyalty.member_service.exception.PointTypeNotFoundException;
import com.loyalty.member_service.feign_client.CustomerClient;
import com.loyalty.member_service.repository.MemberRepository;
import com.loyalty.member_service.repository.PointMasterRepository;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final CustomerClient customerClient;
    private final MemberRepository memberRepository;
    private final PointMasterRepository pointMasterRepository;
    private final StringRedisTemplate redisTemplate;

    private static final long TTL = 10; // minutes

    @Transactional
    public MemberResponse enroll(CustomerRequest request) {

        // 1. Only remote call is protected
        CustomerDto customer = getOrCreateCustomer(request);

        // 2. Pure domain validation (NO circuit breaker here)
        Optional<Member> optional = memberRepository.findByCustomerNumber(customer.getCustomerNumber());
        if (optional.isPresent()) {
            throw new MemberAlreadyExistsException(optional.get().getMembershipNumber());
        }

        // 3. Create member
        Member member = new Member();
        member.setMembershipNumber(generateMembershipNumber());
        member.setCustomerNumber(customer.getCustomerNumber());
        member.setTierLevel("BASIC");
        member.setStatus("ACTIVE");

        memberRepository.save(member);

        return buildResponse(member, request);
    }

    // Circuit breaker ONLY here
    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallback")
    public CustomerDto getOrCreateCustomer(CustomerRequest request) {

        try {
            return customerClient.getByEmail(request.getEmail());
        } catch (FeignException.NotFound ex) {
            return customerClient.createCustomer(request);
        }
    }

    public CustomerDto customerFallback(CustomerRequest request, Throwable ex) {
        log.error("Fallback triggered due to:", ex.getMessage());
        throw new CustomerServiceUnavailableException(
                "Customer Service temporarily unavailable. Please try again later."
        );
    }

    private MemberResponse buildResponse(Member member, CustomerRequest request) {
        MemberResponse response = new MemberResponse();
        response.setMembershipNumber(member.getMembershipNumber());
        response.setCustomerNumber(member.getCustomerNumber());
        response.setTierLevel(member.getTierLevel());
        response.setStatus(member.getStatus());
        response.setEmail(request.getEmail());
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setPhone(request.getPhone());
        response.setCountry(request.getCountry());
        response.setDob(request.getDob());
        response.setAddresses(request.getAddresses());
        return response;
    }

    private String generateMembershipNumber() {
        return "MEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public boolean getPointType(String pointype) {
        return pointMasterRepository.existsByPointType(pointype);
    }

    public PointMasterResponseDTO createPoints(PointMasterRequestDTO pointDetails) {

        if (pointMasterRepository.existsByPointType(pointDetails.getPointType())) {
            throw new PointTypeExistException(pointDetails.getPointType());
        }

        PointMaster master = new PointMaster();
        master.setPointType(pointDetails.getPointType());
        master.setExpiryMonths(pointDetails.getExpiryMonths());
        master.setIsTierQualifying(pointDetails.getIsTierQualifying());
        master.setMaxLimit(pointDetails.getMaxLimit());
        pointMasterRepository.save(master);

        String pointType = master.getPointType();

        redisTemplate.opsForValue().set(pointType, pointType, TTL, TimeUnit.MINUTES);

        PointMasterResponseDTO pointMasterResponseDTO = new PointMasterResponseDTO();
        pointMasterResponseDTO.setPointType(master.getPointType());
        pointMasterResponseDTO.setExpiryMonths(master.getExpiryMonths());
        pointMasterResponseDTO.setIsTierQualifying(master.getIsTierQualifying());
        pointMasterResponseDTO.setMaxLimit(master.getMaxLimit());
        return pointMasterResponseDTO;
    }

    public PointMasterResponseDTO updatePoints(PointMasterRequestDTO pointDetails) {

        Optional<PointMaster> opt = pointMasterRepository.findById(pointDetails.getPointType());
        if (opt.isEmpty()) {
            throw new PointTypeNotFoundException(pointDetails.getPointType());
        }

        PointMaster master = opt.get();
        master.setExpiryMonths(pointDetails.getExpiryMonths());
        master.setIsTierQualifying(pointDetails.getIsTierQualifying());
        master.setMaxLimit(pointDetails.getMaxLimit());

        pointMasterRepository.save(master);

        // refresh cache
        redisTemplate.opsForValue().set(master.getPointType(), master.getPointType(), TTL, TimeUnit.MINUTES);

        PointMasterResponseDTO resp = new PointMasterResponseDTO();
        resp.setPointType(master.getPointType());
        resp.setExpiryMonths(master.getExpiryMonths());
        resp.setIsTierQualifying(master.getIsTierQualifying());
        resp.setMaxLimit(master.getMaxLimit());
        return resp;
    }

    public void deletePoints(String pointType) {
        if (!pointMasterRepository.existsByPointType(pointType)) {
            throw new PointTypeNotFoundException(pointType);
        }

        pointMasterRepository.deleteById(pointType);

        // evict from cache
        redisTemplate.delete(pointType);
    }
}