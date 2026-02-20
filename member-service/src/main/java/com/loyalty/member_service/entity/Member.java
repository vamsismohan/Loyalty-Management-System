package com.loyalty.member_service.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "membership_number", length = 20)
    private String membershipNumber;

    @Column(name = "customer_number", nullable = false, length = 20)
    private String customerNumber;

    @Column(name = "tier_level", nullable = false, length = 20)
    private String tierLevel;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "enrolled_date", nullable = false)
    private LocalDateTime enrolledDate;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.enrolledDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}