package com.loyalty.accrual_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.accrual_service.dto.AccrualRequestDTO;
import com.loyalty.accrual_service.dto.AccrualResponseDTO;
import com.loyalty.accrual_service.service.AccrualService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accruals")
@RequiredArgsConstructor
public class AccrualController {

    private final AccrualService accrualService;

    @PostMapping
    public ResponseEntity<AccrualResponseDTO> createAccrual(@RequestBody AccrualRequestDTO request) {
        AccrualResponseDTO resp = accrualService.processAccrual(request);
        return ResponseEntity.accepted().body(resp);
    }
}
