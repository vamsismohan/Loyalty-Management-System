package com.loyalty.partner_service.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
import com.loyalty.partner_service.exception.PartnerAlreadyExistException;
import com.loyalty.partner_service.exception.PartnerNotFoundException;
import com.loyalty.partner_service.exception.RedemptionActivityRuleNotFoundException;
import com.loyalty.partner_service.repository.PartnerAccrualRuleRepository;
import com.loyalty.partner_service.repository.PartnerMasterRepository;
import com.loyalty.partner_service.repository.PartnerRedemptionRuleRepository;

import jakarta.persistence.criteria.Join;

import java.util.NoSuchElementException;
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
            throw new PartnerAlreadyExistException(request.getPartner());
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
        rule.setPartner(partner);
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
    public AccrualRuleResponseDTO getAccrualByRuleId(String ruleId) {
        PartnerAccrualRule rule = accrualRepository.findById(ruleId)
                .orElseThrow(() -> new NoSuchElementException("Rule not found"));

        return mapToDTO(rule);
    }

    @Transactional(readOnly = true)
    public Page<AccrualRuleResponseDTO> searchAccrualRules(String partner,
            String pointType,
            String unitType,
            Boolean active,
            Pageable pageable) {

        Specification<PartnerAccrualRule> spec = Specification.where(null);

        if (partner != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("partner").get("partner"), partner));
        }

        if (pointType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("pointType"), pointType));
        }

        if (unitType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("unitType"), unitType));
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        return accrualRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    /* CREATE REDEMPTION RULE */

    public RedemptionRuleResponseDTO createRedemptionRule(RedemptionRuleRequestDTO request) {

        PartnerMaster partner = partnerRepository.findById(request.getPartner())
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        PartnerRedemptionRule rule = new PartnerRedemptionRule();
        rule.setRuleId(generateRuleId());
        rule.setPartner(partner);
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
    public Page<RedemptionRuleResponseDTO> searchRedemptionRules(
            String partner,
            String pointType,
            String rewardType,
            Boolean active,
            Pageable pageable) {

        Specification<PartnerRedemptionRule> spec = Specification.where(null);

        if (partner != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("partner").get("partner"), partner));
        }

        if (pointType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("pointType"), pointType));
        }

        if (rewardType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rewardType"), rewardType));
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        return redemptionRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public RedemptionRuleResponseDTO getRedemptionByRuleId(String ruleId) {

        PartnerRedemptionRule rule = redemptionRepository.findById(ruleId)
                .orElseThrow(() -> new NoSuchElementException("Redemption rule not found"));

        return mapToDTO(rule);
    }

    /* RULE ID GENERATOR */

    private String generateRuleId() {
        return "RUL-" + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }

    private AccrualRuleResponseDTO mapToDTO(PartnerAccrualRule rule) {
        AccrualRuleResponseDTO dto = new AccrualRuleResponseDTO();
        dto.setRuleId(rule.getRuleId());
        dto.setPartner(rule.getPartner().getPartner());
        dto.setPointType(rule.getPointType());
        dto.setPointsPerUnit(rule.getPointsPerUnit());
        dto.setUnitType(rule.getUnitType());
        dto.setActive(rule.getActive());
        return dto;
    }

    private RedemptionRuleResponseDTO mapToDTO(PartnerRedemptionRule rule) {

        RedemptionRuleResponseDTO dto = new RedemptionRuleResponseDTO();
        dto.setRuleId(rule.getRuleId());
        dto.setPartner(rule.getPartner().getPartner());
        dto.setPointType(rule.getPointType());
        dto.setPointsRequired(rule.getPointsRequired());
        dto.setRewardType(rule.getRewardType());
        dto.setActive(rule.getActive());

        return dto;
    }
}