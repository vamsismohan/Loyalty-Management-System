package com.loyalty.partner_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.loyalty.partner_service.entity.PartnerRedemptionRule;

public interface PartnerRedemptionRuleRepository extends JpaRepository<PartnerRedemptionRule, String>{

    Optional<PartnerRedemptionRule> findByPartner_PartnerAndPointTypeAndRewardTypeAndActiveTrue(String partner,
            String pointType, String rewardType);

}
