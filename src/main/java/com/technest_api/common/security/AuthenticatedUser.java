package com.technest_api.common.security;

import com.technest_api.common.constant.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AuthenticatedUser {
    private UUID id;
    private String email;
    private Role role;
}
