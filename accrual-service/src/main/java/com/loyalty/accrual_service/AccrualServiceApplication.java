package com.loyalty.accrual_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccrualServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccrualServiceApplication.class, args);
    }
}
