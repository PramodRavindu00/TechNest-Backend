package com.technest_api.module.post;

import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.post.dto.request.CreatePostRequest;
import com.technest_api.module.post.dto.request.PinPostRequest;
import com.technest_api.module.post.dto.request.UpdatePostRequest;
import com.technest_api.module.post.dto.response.PostFullResponse;
import com.technest_api.module.post.dto.response.PostSummaryResponse;
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

    @GetMapping()
    public ResponseEntity<List<PostFullResponse>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostFullResponse> getOne(@PathVariable String id) {
        return ResponseEntity.ok(postService.getOne(id));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<PostSummaryResponse>> getMyAll(
            @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(postService.getAllByUser(user.getId()));
    }

    @GetMapping("/mine/{id}")
    public ResponseEntity<PostSummaryResponse> getMyOne(@PathVariable String id,
                                                        @AuthenticationPrincipal
                                                        AuthenticatedUser user) {
        return ResponseEntity.ok(postService.getOneByUser(id, user.getId()));
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

    @PatchMapping("/{id}/pin")
    @PreAuthorize(
            "hasAnyRole('AUTHOR', 'MODERATOR', 'ADMIN') && @PostSecurity.isResourceOwner(#id, principal)")
    public void updatePostPinStatus(@PathVariable UUID id,
                                    @Valid @RequestBody PinPostRequest request) {
    }

    @PatchMapping("/{id}/feature")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public void updatePostFeatureStatus(@PathVariable UUID id,
                                        @Valid @RequestBody PinPostRequest request) {
    }


}
