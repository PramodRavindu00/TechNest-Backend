package com.technest_api.module.post.dto.request;

import jakarta.validation.constraints.NotNull;

public class FeaturePostRequest {
    @NotNull
    Boolean isFeatured;
}
