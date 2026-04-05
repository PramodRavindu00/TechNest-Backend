package com.technest_api.module.post.dto;

import jakarta.validation.constraints.NotNull;

public class FeaturePostRequest {
    @NotNull
    Boolean isFeatured;
}
