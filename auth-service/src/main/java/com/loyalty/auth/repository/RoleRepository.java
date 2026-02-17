package com.loyalty.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loyalty.auth.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
