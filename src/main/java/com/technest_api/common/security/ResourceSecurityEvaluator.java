package com.technest_api.common.security;

public interface ResourceSecurityEvaluator {
    boolean isResourceOwner(String resourceId, AuthenticatedUser currentUser);
}
