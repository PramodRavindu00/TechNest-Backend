package com.technest_api.module.post.security;

import com.technest_api.common.constant.enums.AccessDeniedCode;
import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.common.security.ResourceSecurityEvaluator;
import com.technest_api.module.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("PostSecurity")
@RequiredArgsConstructor
public class PostSecurityEvaluator implements ResourceSecurityEvaluator {
    private final PostRepository postRepo;

    @Override
    public boolean isResourceOwner(String resourceId, AuthenticatedUser currentUser) {
        postRepo.findByIdAndAuthorId(UUID.fromString(resourceId), currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException(
                        AccessDeniedCode.NOT_RESOURCE_OWNER.name()));
        return true;
    }
}
