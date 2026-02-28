package com.loyalty.member_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loyalty.member_service.entity.PointMaster;


@Repository
public interface PointMasterRepository extends JpaRepository<PointMaster, String>{

    boolean existsByPointType(String pointType);
}
