package com.technest_api.module.post;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.request.CreatePostRequest;
import com.technest_api.module.post.dto.request.PostPayload;
import com.technest_api.module.post.dto.request.UpdatePostRequest;
import com.technest_api.module.post.dto.response.PostFullResponse;
import com.technest_api.module.post.dto.response.PostSummaryResponse;
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
    public void create(CreatePostRequest request, AuthenticatedUser user) {
        Post newPost = buildPost(request, user, null).build();

        // save the new post
        postRepo.save(newPost);

        //update if the user role is a reader to author
        userService.promoteToAuthorIfReader(user);
    }

    public void edit(UUID id, UpdatePostRequest request, AuthenticatedUser user) {
        Post existing = postRepo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Post updatedPost = buildPost(request, user, existing).build();

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
    public List<PostFullResponse> getAll() {
        List<Post> posts = postRepo.findAll();
        return postMapper.toFullDtoList(posts);
    }

    public List<PostSummaryResponse> getAllByUser(UUID userId) {
        List<Post> posts = postRepo.findAllByAuthorId(userId);
        return postMapper.toSummaryDtoList(posts);
    }


    public PostFullResponse getOne(String id) {
        Post post = postRepo.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return postMapper.toFullDto(post);
    }

    public PostSummaryResponse getOneByUser(String id, UUID userId) {
        Post post = postRepo.findByIdAndAuthorId(UUID.fromString(id), userId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return postMapper.toSummaryDto(post);
    }


    private Post.PostBuilder buildPost(PostPayload request, AuthenticatedUser user,
                                       Post existingPost) {
        // sanitize the body
        String safeBody = Jsoup.clean(request.getBody(), Safelist.basicWithImages());

        // generate a unique slug if it's pure creation or if it's not equals to current slug only
        // if it's editing both titles are exactly same then use the existing slug
        String slug = existingPost != null && existingPost.getTitle()
                .equals(request.getTitle()) ? existingPost.getSlug() :
                generateUniqueSlug(request.getTitle());

        Post.PostBuilder builder = Post.builder()
                .title(request.getTitle())
                .authorId(existingPost != null ? existingPost.getAuthorId() : user.getId())
                .slug(slug)
                .excerpt(generateExcerpt(safeBody))
                .body(safeBody)
                .status(request.getStatus() != null ? request.getStatus() : PostStatus.DRAFT)
                .publishedAt(
                        request.getStatus() == PostStatus.PUBLISHED ? LocalDateTime.now() : null);

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
