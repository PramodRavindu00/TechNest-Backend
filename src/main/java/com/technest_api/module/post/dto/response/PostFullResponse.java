package com.technest_api.module.post.dto.response;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.module.post.dto.projection.FullPostProjection;
import com.technest_api.module.post.model.PostMedia;
import com.technest_api.module.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFullResponse implements FullPostProjection {

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

    private UserResponse author;
    private List<PostMedia> media;
}
