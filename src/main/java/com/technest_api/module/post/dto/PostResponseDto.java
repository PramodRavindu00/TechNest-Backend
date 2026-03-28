package com.technest_api.module.post.dto;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.module.post.model.PostMedia;
import com.technest_api.module.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private UUID id;
    private String title;
    private String slug;
    private String excerpt;
    private String body;
    private UserResponseDto author;
    private List<PostMedia> media;
    private PostStatus status;
    private Boolean featured;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
