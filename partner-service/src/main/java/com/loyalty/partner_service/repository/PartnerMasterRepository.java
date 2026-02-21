package com.loyalty.partner_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.loyalty.partner_service.entity.PartnerMaster;

@Repository
public interface PartnerMasterRepository extends JpaRepository<PartnerMaster, String> {

}
