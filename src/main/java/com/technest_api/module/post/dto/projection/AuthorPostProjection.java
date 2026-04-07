package com.technest_api.module.post.dto.projection;

import java.util.UUID;

public interface AuthorPostProjection extends PostProjection {
    UUID getAuthorId();
}
