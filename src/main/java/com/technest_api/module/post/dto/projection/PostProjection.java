package com.technest_api.module.post.dto.projection;

import com.technest_api.common.constant.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PostProjection {
    UUID getId();

    String getTitle();

    String getSlug();

    String getExcerpt();

    String getBody();

    PostStatus getStatus();

    Boolean getFeatured();

    Integer getLikesCount();

    Integer getCommentsCount();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getPublishedAt();

}
