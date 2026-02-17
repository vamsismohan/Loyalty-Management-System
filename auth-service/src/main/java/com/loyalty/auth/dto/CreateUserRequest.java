package com.loyalty.auth.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private List<Long> roleIds;
}

