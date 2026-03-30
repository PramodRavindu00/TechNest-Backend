package com.technest_api.module.post.dto;

import com.technest_api.common.constant.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePostDto implements PostPayload {

    @NotBlank()
    private String title;

    @NotBlank()
    private String body;

    private PostStatus status;

}
