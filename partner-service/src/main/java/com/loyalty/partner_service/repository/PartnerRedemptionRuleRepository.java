package com.loyalty.partner_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.loyalty.partner_service.entity.PartnerRedemptionRule;

@Repository
public interface PartnerRedemptionRuleRepository
        extends JpaRepository<PartnerRedemptionRule, String>, JpaSpecificationExecutor<PartnerRedemptionRule> {

}
