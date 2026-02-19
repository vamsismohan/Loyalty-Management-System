package com.loyalty.auth.dto;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

@Data
public class RoleRequest {

    @NonNull
    private String roleName;
}
