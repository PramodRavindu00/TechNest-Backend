package com.technest_api.module.post.dto.response;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.module.post.dto.projection.AuthorPostProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryResponse implements AuthorPostProjection {
    private UUID id;
    private String title;
    private String slug;
    private String excerpt;
    private String body;
    private PostStatus status;
    private Boolean featured;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Nullable
    private LocalDateTime publishedAt;
    private UUID authorId;
}
