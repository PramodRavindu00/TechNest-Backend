package com.technest_api.module.post.dto.projection;

import com.technest_api.module.post.model.PostMedia;
import com.technest_api.module.user.dto.response.UserResponse;

import java.util.List;

public interface FullPostProjection extends PostProjection {
    UserResponse getAuthor();

    List<PostMedia> getMedia();
}
