package com.loyalty.partner_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loyalty.partner_service.entity.PartnerAccrualRule;

@Repository
public interface PartnerAccrualRuleRepository extends JpaRepository<PartnerAccrualRule, String>{

    Optional<PartnerAccrualRule> findByPartnerMaster_PartnerAndPointTypeAndUnitTypeAndActiveTrue(String partner, String pointType,
            String unitType);

}
