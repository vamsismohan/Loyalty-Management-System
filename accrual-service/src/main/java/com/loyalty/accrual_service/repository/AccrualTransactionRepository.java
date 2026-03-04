package com.loyalty.accrual_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loyalty.accrual_service.entity.AccrualTransaction;

public interface AccrualTransactionRepository extends JpaRepository<AccrualTransaction, Long> {

}
