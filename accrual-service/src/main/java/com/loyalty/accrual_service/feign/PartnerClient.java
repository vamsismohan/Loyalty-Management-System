package com.loyalty.accrual_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.loyalty.accrual_service.feign.dto.PartnerAccrualRuleDTO;

@FeignClient(name = "partner-service", url = "http://localhost:8080")
public interface PartnerClient {

    @GetMapping("/partners/{partner}/accrual-rule/{pointType}")
    PartnerAccrualRuleDTO getAccrualRule(@PathVariable("partner") String partner, @PathVariable("pointType") String pointType);
}
