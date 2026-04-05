package com.technest_api.module.post.dto;

import jakarta.validation.constraints.NotNull;

public class PinPostRequest {
    @NotNull
    Boolean isPinned;
}
