package com.loyalty.partner_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(
    name = "partner_redemption_rule",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_partner_redemption",
        columnNames = {"partner", "point_type", "reward_type"}
    )
)
@Getter
@Setter
public class PartnerRedemptionRule {

    @Id
    @Column(name = "rule_id", length = 20)
    private String ruleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner", nullable = false)
    private PartnerMaster partnerMaster;

    @Column(name = "point_type", nullable = false, length = 50)
    private String pointType;

    @Column(name = "points_required", nullable = false)
    private Long pointsRequired;

    @Column(name = "reward_type", nullable = false, length = 50)
    private String rewardType;

    @Column(name = "active", nullable = false)
    private Boolean active;
}