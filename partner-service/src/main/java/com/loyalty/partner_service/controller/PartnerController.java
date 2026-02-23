package com.loyalty.partner_service.controller;

import com.loyalty.partner_service.dto.*;
import com.loyalty.partner_service.service.PartnerService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                                partnerService.createPartner(request));
        }

        @PostMapping("/accrual-rules")
        public ResponseEntity<AccrualRuleResponseDTO> createAccrualRule(
                        @RequestBody AccrualRuleRequestDTO request) {

                return ResponseEntity.ok(
                                partnerService.createAccrualRule(request));
        }

        @GetMapping("/accrual-rules")
        public Page<AccrualRuleResponseDTO> getAllAccrualRules(
                        @RequestParam(required = false) String partner,
                        @RequestParam(required = false) String pointType,
                        @RequestParam(required = false) String unitType,
                        @RequestParam(required = false) Boolean active,
                        Pageable pageable) {
                return partnerService.searchAccrualRules(partner, pointType, unitType, active, pageable);
        }

        @GetMapping("/accrual-rules/{ruleId}")
        public AccrualRuleResponseDTO getAccrualRule(@PathVariable String ruleId) {
                return partnerService.getAccrualByRuleId(ruleId);
        }

        @PostMapping("/redemption-rules")
        public ResponseEntity<RedemptionRuleResponseDTO> createRedemptionRule(
                        @RequestBody RedemptionRuleRequestDTO request) {

                return ResponseEntity.ok(
                                partnerService.createRedemptionRule(request));
        }

        @GetMapping("/redemption-rules")
        public Page<RedemptionRuleResponseDTO> getAllRedemptionRules(
                        @RequestParam(required = false) String partner,
                        @RequestParam(required = false) String pointType,
                        @RequestParam(required = false) String rewardType,
                        @RequestParam(required = false) Boolean active,
                        Pageable pageable) {

                return partnerService.searchRedemptionRules(
                                partner,
                                pointType,
                                rewardType,
                                active,
                                pageable);
        }

        @GetMapping("/redemption-rules/{ruleId}")
        public RedemptionRuleResponseDTO getRedemptionRule(
                        @PathVariable String ruleId) {

                return partnerService.getRedemptionByRuleId(ruleId);
        }
}
