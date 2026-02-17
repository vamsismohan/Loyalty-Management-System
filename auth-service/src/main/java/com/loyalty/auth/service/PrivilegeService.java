package com.loyalty.auth.service;

import org.springframework.stereotype.Service;

import com.loyalty.auth.entity.Privilege;
import com.loyalty.auth.repository.PrivilegeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public Privilege createPrivilege(String privilegeName) {

        Privilege privilege = new Privilege();
        privilege.setPrivilegeName(privilegeName);

        return privilegeRepository.save(privilege);
    }
}

