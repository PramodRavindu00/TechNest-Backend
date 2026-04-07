package com.technest_api.module.post;

import com.technest_api.module.post.model.Post;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Boolean existsBySlug(String slug);

    @EntityGraph(value = "Post.withRelations", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    @NonNull
    List<Post> findAll();

    List<Post> findAllByAuthorId(UUID authorId);

    Optional<Post> findByIdAndAuthorId(UUID id, UUID authorId);

    @Modifying
    int deletePostById(UUID id);
}

