package com.loyalty.member_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_master")
@Getter
@Setter
@NoArgsConstructor
public class PointMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_type")
    private Long pointType;

    @Column(name = "is_tier_qualifying")
    private Boolean isTierQualifying;

    @Column(name = "expiry_months")
    private Integer expiryMonths;

    @Column(name = "max_limit")
    private Long maxLimit;
}