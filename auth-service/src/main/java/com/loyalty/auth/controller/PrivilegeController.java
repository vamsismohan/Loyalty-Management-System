package com.loyalty.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.auth.dto.PrivilegeRequest;
import com.loyalty.auth.entity.Privilege;
import com.loyalty.auth.service.PrivilegeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/privileges")
@RequiredArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @PostMapping
    public Privilege createPrivilege(
            @RequestBody PrivilegeRequest request) {
        return privilegeService.createPrivilege(
                request.getPrivilegeName());
    }
}

