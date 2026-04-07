package com.technest_api.module.user.dto.response;

import com.technest_api.common.constant.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String googleId;
    private String linkedinId;
    private Role role;
}
