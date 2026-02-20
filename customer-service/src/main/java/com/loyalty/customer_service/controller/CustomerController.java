package com.loyalty.customer_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.customer_service.dto.CustomerDto;
import com.loyalty.customer_service.dto.CustomerRequest;
import com.loyalty.customer_service.dto.CustomerResponse;
import com.loyalty.customer_service.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(service.createCustomer(request));
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable String customerNumber) {

        return ResponseEntity.ok(service.getCustomer(customerNumber));
    }

    @PutMapping("/{customerNumber}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String customerNumber,
            @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(service.updateCustomer(customerNumber, request));
    }

    @DeleteMapping("/{customerNumber}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable String customerNumber) {

        service.deleteCustomer(customerNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

}
