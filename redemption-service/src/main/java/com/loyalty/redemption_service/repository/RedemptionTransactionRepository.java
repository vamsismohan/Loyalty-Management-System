package com.loyalty.redemption_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loyalty.redemption_service.entity.RedemptionTransaction;

public interface RedemptionTransactionRepository extends JpaRepository<RedemptionTransaction, Long> {

}
