package com.loyalty.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.auth.dto.RoleRequest;
import com.loyalty.auth.entity.Role;
import com.loyalty.auth.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public Role createRole(@RequestBody RoleRequest request) {
        return roleService.createRole(request.getRoleName());
    }

    @PostMapping("/{roleId}/privileges")
    public void assignPrivileges(@PathVariable Long roleId,
        @RequestBody List<Long> privilegeIds) {
    roleService.assignPrivileges(roleId, privilegeIds);
}

}
