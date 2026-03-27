package com.technest_api.module.post;

import com.technest_api.common.constant.enums.PostStatus;
import com.technest_api.common.constant.enums.Role;
import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.CreatePostDto;
import com.technest_api.module.post.model.Post;
import com.technest_api.module.user.UserService;
import com.technest_api.module.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserService userService;

    @Transactional
    public void create(CreatePostDto dto, AuthenticatedUser user) {

        //sanitizing the body
        String safeBody = Jsoup.clean(dto.getBody(), Safelist.basicWithImages());

        Post newPost = Post.builder()
                .authorId(user.getId())
                .title(dto.getTitle())
                .slug(generateUniqueSlug(dto.getTitle()))
                .excerpt(generateExcerpt(safeBody))
                .body(safeBody)
                .status(dto.getStatus() != null ? dto.getStatus() : PostStatus.DRAFT)
                .build();

        // save the new post
        postRepo.save(newPost);

        //update user if the user role is a reader to author
        User requestingUser = userService.findById(user.getId()
                        .toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User Not " + "Found"));

        //if role type is reader only update role as AUTHOR
        if (requestingUser.getRole() == Role.READER) {

        }

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
