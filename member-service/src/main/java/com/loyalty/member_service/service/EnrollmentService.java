package com.loyalty.member_service.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.loyalty.member_service.dto.CustomerDto;
import com.loyalty.member_service.dto.CustomerRequest;
import com.loyalty.member_service.dto.MemberResponse;
import com.loyalty.member_service.entity.Member;
import com.loyalty.member_service.exception.CustomerServerSideError;
import com.loyalty.member_service.exception.CustomerServiceUnavailableException;
import com.loyalty.member_service.exception.MemberAlreadyExistsException;
import com.loyalty.member_service.feign_client.CustomerClient;
import com.loyalty.member_service.repository.MemberRepository;

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
        } catch (FeignException ex) {
            throw new CustomerServerSideError(ex.getMessage());
        }
    }

    public CustomerDto customerFallback(CustomerRequest request, Throwable ex) {
        log.error("Fallback triggered due to:", ex);
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
}