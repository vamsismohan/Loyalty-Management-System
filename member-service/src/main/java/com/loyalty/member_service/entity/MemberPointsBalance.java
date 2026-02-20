package com.loyalty.member_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_points_balance")
@Getter
@Setter
@NoArgsConstructor
public class MemberPointsBalance {

    @EmbeddedId
    private MemberPointsBalanceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("membershipNumber")
    @JoinColumn(name = "membership_number", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pointTypeId")
    @JoinColumn(name = "point_type_id", nullable = false)
    private PointType pointType;

    @Column(name = "balance", nullable = false)
    private Long balance;
}