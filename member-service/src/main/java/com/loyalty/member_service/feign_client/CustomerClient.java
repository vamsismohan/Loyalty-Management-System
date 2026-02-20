package com.loyalty.member_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.loyalty.member_service.dto.CustomerDto;
import com.loyalty.member_service.dto.CustomerRequest;

@FeignClient(
        name = "customer-service",
        url = "${customer.service.url}"
)
public interface CustomerClient {

    @GetMapping("/customers/email/{email}")
    CustomerDto getByEmail(@PathVariable String email);

    @PostMapping("/customers")
    CustomerDto createCustomer(@RequestBody CustomerRequest request);
}
