package com.loyalty.partner_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loyalty.partner_service.dto.AccrualRuleRequestDTO;
import com.loyalty.partner_service.dto.AccrualRuleResponseDTO;
import com.loyalty.partner_service.dto.PartnerRequestDTO;
import com.loyalty.partner_service.dto.PartnerResponseDTO;
import com.loyalty.partner_service.dto.RedemptionRuleRequestDTO;
import com.loyalty.partner_service.dto.RedemptionRuleResponseDTO;
import com.loyalty.partner_service.entity.PartnerAccrualRule;
import com.loyalty.partner_service.entity.PartnerMaster;
import com.loyalty.partner_service.entity.PartnerRedemptionRule;
import com.loyalty.partner_service.exception.AccrualActivityRuleNotFoundException;
import com.loyalty.partner_service.exception.PartnerNotFoundException;
import com.loyalty.partner_service.exception.RedemptionActivityRuleNotFoundException;
import com.loyalty.partner_service.repository.PartnerAccrualRuleRepository;
import com.loyalty.partner_service.repository.PartnerMasterRepository;
import com.loyalty.partner_service.repository.PartnerRedemptionRuleRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService {

    private final PartnerMasterRepository partnerRepository;
    private final PartnerAccrualRuleRepository accrualRepository;
    private final PartnerRedemptionRuleRepository redemptionRepository;

    /* CREATE PARTNER */

    public PartnerResponseDTO createPartner(PartnerRequestDTO request) {

        if (partnerRepository.existsById(request.getPartner())) {
            throw new PartnerNotFoundException(request.getPartner());
        }

        PartnerMaster entity = new PartnerMaster();
        entity.setPartner(request.getPartner());
        entity.setPartnerType(request.getPartnerType());
        entity.setStatus(request.getStatus());

        partnerRepository.save(entity);

        return PartnerResponseDTO.builder()
                .partner(entity.getPartner())
                .partnerType(entity.getPartnerType())
                .status(entity.getStatus())
                .build();
    }

    /* CREATE ACCRUAL RULE */

    public AccrualRuleResponseDTO createAccrualRule(AccrualRuleRequestDTO request) {

        PartnerMaster partner = partnerRepository.findById(request.getPartner())
                .orElseThrow(() -> new PartnerNotFoundException(request.getPartner()));

        PartnerAccrualRule rule = new PartnerAccrualRule();
        rule.setRuleId(generateRuleId());
        rule.setPartnerMaster(partner);
        rule.setPointType(request.getPointType());
        rule.setPointsPerUnit(request.getPointsPerUnit());
        rule.setUnitType(request.getUnitType());
        rule.setActive(request.getActive() != null ? request.getActive() : true);

        accrualRepository.save(rule);

        return AccrualRuleResponseDTO.builder()
                .ruleId(rule.getRuleId())
                .partner(partner.getPartner())
                .pointType(rule.getPointType())
                .pointsPerUnit(rule.getPointsPerUnit())
                .unitType(rule.getUnitType())
                .active(rule.getActive())
                .build();
    }

    /* GET ACTIVE ACCRUAL RULE */

    @Transactional(readOnly = true)
    public AccrualRuleResponseDTO getActiveAccrualRule(
            String partner,
            String pointType,
            String unitType) {

        Optional<PartnerAccrualRule> rule = accrualRepository.findByPartnerMaster_PartnerAndPointTypeAndUnitTypeAndActiveTrue(
                partner, pointType, unitType);

        if (rule.isPresent()) {
            return AccrualRuleResponseDTO.builder()
                    .ruleId(rule.get().getRuleId())
                    .partner(rule.get().getPartnerMaster().getPartner())
                    .pointType(rule.get().getPointType())
                    .pointsPerUnit(rule.get().getPointsPerUnit())
                    .unitType(rule.get().getUnitType())
                    .active(rule.get().getActive())
                    .build();
        } else {
            throw new AccrualActivityRuleNotFoundException("Active rule not found");
        }
    }

    /* CREATE REDEMPTION RULE */

    public RedemptionRuleResponseDTO createRedemptionRule(RedemptionRuleRequestDTO request) {

        PartnerMaster partner = partnerRepository.findById(request.getPartner())
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        PartnerRedemptionRule rule = new PartnerRedemptionRule();
        rule.setRuleId(generateRuleId());
        rule.setPartnerMaster(partner);
        rule.setPointType(request.getPointType());
        rule.setPointsRequired(request.getPointsRequired());
        rule.setRewardType(request.getRewardType());
        rule.setActive(request.getActive() != null ? request.getActive() : true);

        redemptionRepository.save(rule);

        return RedemptionRuleResponseDTO.builder()
                .ruleId(rule.getRuleId())
                .partner(partner.getPartner())
                .pointType(rule.getPointType())
                .pointsRequired(rule.getPointsRequired())
                .rewardType(rule.getRewardType())
                .active(rule.getActive())
                .build();
    }

    @Transactional(readOnly = true)
    public RedemptionRuleResponseDTO getRedemptionActivityRule(
            String partner,
            String pointType,
            String rewardType) {

        Optional<PartnerRedemptionRule> rule = redemptionRepository
                .findByPartnerMaster_PartnerAndPointTypeAndRewardTypeAndActiveTrue(
                        partner,
                        pointType,
                        rewardType);
        if(rule.isEmpty()) {
            throw new RedemptionActivityRuleNotFoundException("Active redemption rule not found");
        }

        return RedemptionRuleResponseDTO.builder()
                .ruleId(rule.get().getRuleId())
                .partner(rule.get().getPartnerMaster().getPartner())
                .pointType(rule.get().getPointType())
                .pointsRequired(rule.get().getPointsRequired())
                .rewardType(rule.get().getRewardType())
                .active(rule.get().getActive())
                .build();
    }

    /* RULE ID GENERATOR */

    private String generateRuleId() {
        return "RUL-" + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }
}