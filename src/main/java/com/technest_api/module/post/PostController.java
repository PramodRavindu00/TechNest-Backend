package com.technest_api.module.post;

import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.CreatePostDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreatePostDto dto,
                       @AuthenticationPrincipal AuthenticatedUser user) {
        postService.create(dto, user);
    }

}
