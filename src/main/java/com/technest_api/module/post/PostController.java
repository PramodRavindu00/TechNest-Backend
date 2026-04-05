package com.technest_api.module.post;

import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.CreatePostRequest;
import com.technest_api.module.post.dto.PinPostRequest;
import com.technest_api.module.post.dto.PostResponseDto;
import com.technest_api.module.post.dto.UpdatePostRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreatePostRequest request,
                       @AuthenticationPrincipal AuthenticatedUser user) {
        postService.create(request, user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('AUTHOR', 'MODERATOR', 'ADMIN') && @PostSecurity.isResourceOwner(#id, principal)")
    public void edit(@PathVariable UUID id, @Valid @RequestBody UpdatePostRequest request,
                     @AuthenticationPrincipal AuthenticatedUser user) {
        postService.edit(id, request, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('AUTHOR', 'MODERATOR', 'ADMIN') && @PostSecurity.isResourceOwner(#id, principal)")
    public void delete(@PathVariable UUID id) {
        postService.delete(id);
    }

    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getOne(@PathVariable String id) {
        return ResponseEntity.ok(postService.getOne(id));
    }

    public void updatePostPinStatus(@PathVariable UUID id,
                                    @Valid @RequestBody PinPostRequest request) {
    }
}
