package com.loyalty.member_service.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberPointsBalanceId implements Serializable {

    @Column(name = "membership_number", length = 20)
    private String membershipNumber;

    @Column(name = "point_type")
    private String pointType;
}
