package com.loyalty.partner_service.controller;

import com.loyalty.partner_service.dto.*;
import com.loyalty.partner_service.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;


    @PostMapping
    public ResponseEntity<PartnerResponseDTO> createPartner(
            @RequestBody PartnerRequestDTO request) {

        return ResponseEntity.ok(
                partnerService.createPartner(request)
        );
    }

    @PostMapping("/accrual-rules")
    public ResponseEntity<AccrualRuleResponseDTO> createAccrualRule(
            @RequestBody AccrualRuleRequestDTO request) {

        return ResponseEntity.ok(
                partnerService.createAccrualRule(request)
        );
    }

    @GetMapping("/accrual-rules")
    public ResponseEntity<AccrualRuleResponseDTO> getAccrualRule(
            @RequestParam String partner,
            @RequestParam String pointType,
            @RequestParam String unitType) {

        return ResponseEntity.ok(
                partnerService.getActiveAccrualRule(
                        partner,
                        pointType,
                        unitType
                )
        );
    }

    @PostMapping("/redemption-rules")
    public ResponseEntity<RedemptionRuleResponseDTO> createRedemptionRule(
            @RequestBody RedemptionRuleRequestDTO request) {

        return ResponseEntity.ok(
                partnerService.createRedemptionRule(request)
        );
    }

    @GetMapping("/redemption-rules")
    public ResponseEntity<RedemptionRuleResponseDTO> getRedemptionRule(
            @RequestParam String partner,
            @RequestParam String pointType,
            @RequestParam String rewardType) {

        return ResponseEntity.ok(
                partnerService.getRedemptionActivityRule(
                        partner,
                        pointType,
                        rewardType
                )
        );
    }
}
