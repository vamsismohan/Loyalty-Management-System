package com.loyalty.partner_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "member-service",
    url= "${member.service.url}"
)
public interface PartnerFeignClient {

    @GetMapping("/member/pointType/{pointype}")
    boolean getPoints(@PathVariable String pointype); 

}