package com.loyalty.redemption_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.loyalty.redemption_service.feign.dto.PartnerRedemptionRuleDTO;

@FeignClient(name = "partner-service", url = "http://localhost:8080")
public interface PartnerClient {

    @GetMapping("/partners/{partner}/redemption-rule/{pointType}")
    PartnerRedemptionRuleDTO getRedemptionRule(@PathVariable("partner") String partner, @PathVariable("pointType") String pointType);
}
