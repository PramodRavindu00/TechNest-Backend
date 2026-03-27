package com.technest_api.module.post;

import com.technest_api.module.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Boolean existsBySlug(String slug);
}
