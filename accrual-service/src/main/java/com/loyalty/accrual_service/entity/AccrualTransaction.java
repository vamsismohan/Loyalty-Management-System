package com.loyalty.accrual_service.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "accrual_transaction")
@Data
public class AccrualTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "membership_number")
    private String membershipNumber;

    private String partner;

    private String pointType;

    private String activityType;

    private Long activityValue;

    private String cabinClass;

    private Long pointsEarned;

    private String referenceId;

    private String status;

    private Instant createdAt = Instant.now();
}
