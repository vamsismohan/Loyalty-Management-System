package com.loyalty.redemption_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.redemption_service.dto.RedemptionRequestDTO;
import com.loyalty.redemption_service.dto.RedemptionResponseDTO;
import com.loyalty.redemption_service.service.RedemptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/redemptions")
@RequiredArgsConstructor
public class RedemptionController {

    private final RedemptionService redemptionService;

    @PostMapping
    public ResponseEntity<RedemptionResponseDTO> createRedemption(@RequestBody RedemptionRequestDTO request) {
        RedemptionResponseDTO resp = redemptionService.processRedemption(request);
        return ResponseEntity.accepted().body(resp);
    }
}
