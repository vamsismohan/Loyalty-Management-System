package com.loyalty.partner_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "partner_master")
@Getter
@Setter
public class PartnerMaster {

    @Id
    @Column(name = "partner", length = 100)
    private String partner;

    @Column(name = "partner_type", nullable = false, length = 50)
    private String partnerType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;
}