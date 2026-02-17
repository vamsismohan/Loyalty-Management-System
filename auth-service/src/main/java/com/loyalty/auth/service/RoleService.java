package com.loyalty.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loyalty.auth.entity.Privilege;
import com.loyalty.auth.entity.Role;
import com.loyalty.auth.entity.RolePrivilege;
import com.loyalty.auth.repository.PrivilegeRepository;
import com.loyalty.auth.repository.RolePrivilegeRepository;
import com.loyalty.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    public Role createRole(String roleName) {

        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

    public void assignPrivileges(Long roleId,
                                 List<Long> privilegeIds) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow();

        for (Long pid : privilegeIds) {

            Privilege privilege =
                    privilegeRepository.findById(pid)
                            .orElseThrow();

            rolePrivilegeRepository.save(
                    new RolePrivilege(null, role, privilege)
            );
        }
    }
}
