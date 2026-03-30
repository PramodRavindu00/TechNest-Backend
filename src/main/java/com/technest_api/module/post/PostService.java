package com.technest_api.module.post;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.CreatePostDto;
import com.technest_api.module.post.dto.PostPayload;
import com.technest_api.module.post.dto.PostResponseDto;
import com.technest_api.module.post.dto.UpdatePostDto;
import com.technest_api.module.post.mapper.PostMapper;
import com.technest_api.module.post.model.Post;
import com.technest_api.module.user.UserService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserService userService;
    private final PostMapper postMapper;

    @Transactional
    public void create(CreatePostDto dto, AuthenticatedUser user) {
        Post newPost = buildPost(dto, user, null).build();

        // save the new post
        postRepo.save(newPost);

        //update if the user role is a reader to author
        userService.promoteToAuthorIfReader(user);
    }

    public void edit(UUID id, UpdatePostDto dto, AuthenticatedUser user) {
        Post existing = postRepo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Post updatedPost = buildPost(dto, user, existing).build();

        // save the new post
        postRepo.save(updatedPost);
    }

    @Transactional
    public void delete(UUID id) {
        int affected = postRepo.deletePostById(id);
        if (affected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAll() {
        List<Post> posts = postRepo.findAll();
        return postMapper.toDtoList(posts);
    }

    public PostResponseDto getOne(String id) {
        Post post = postRepo.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return postMapper.toDto(post);
    }

    private Post.PostBuilder buildPost(PostPayload dto, AuthenticatedUser user, Post existingPost) {
        // sanitize the body
        String safeBody = Jsoup.clean(dto.getBody(), Safelist.basicWithImages());

        // generate a unique slug if it's pure creation or if it's not equals to current slug only
        // if it's editing both titles are exactly same then use the existing slug
        String slug = existingPost != null && existingPost.getTitle()
                .equals(dto.getTitle()) ? existingPost.getSlug() :
                generateUniqueSlug(dto.getTitle());

        Post.PostBuilder builder = Post.builder()
                .title(dto.getTitle())
                .authorId(existingPost != null ? existingPost.getAuthorId() : user.getId())
                .slug(slug)
                .excerpt(generateExcerpt(safeBody))
                .body(safeBody)
                .status(dto.getStatus() != null ? dto.getStatus() : PostStatus.DRAFT)
                .publishedAt(dto.getStatus() == PostStatus.PUBLISHED ? LocalDateTime.now() : null);

        // explicitly set existing values to the builder when its editing
        if (existingPost != null) {
            builder.id(existingPost.getId())
                    .createdAt(existingPost.getCreatedAt());
        }
        return builder;
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
        if (postRepo.existsBySlug(baseSlug)) {
            String randomCode = UUID.randomUUID()
                    .toString()
                    .substring(0, 4);

            return baseSlug + "-" + randomCode;
        }
        return baseSlug;
    }

    private String generateExcerpt(String safeBody) {
        return Jsoup.parse(safeBody)
                .text()
                .substring(0, Math.min(150, safeBody.length()));
    }
}
