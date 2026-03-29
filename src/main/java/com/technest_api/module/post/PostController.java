package com.technest_api.module.post;

import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.CreatePostDto;
import com.technest_api.module.post.dto.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public void edit(@Valid @RequestBody CreatePostDto dto,
                     @AuthenticationPrincipal AuthenticatedUser user) {
        postService.create(dto, user);
    }


    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('AUTHOR', 'MODERATOR', 'ADMIN') && @PostSecurity.isResourceOwner(#id, principal)")
    public ResponseEntity<PostResponseDto> getOne(@PathVariable String id) {
        return ResponseEntity.ok(postService.getOne(id));
    }
}
