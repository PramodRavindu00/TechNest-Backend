package com.technest_api.module.post.dto.request;

import com.technest_api.common.constant.enums.PostStatus;

public interface PostPayload {
    String getTitle();

    String getBody();

    PostStatus getStatus();
}
