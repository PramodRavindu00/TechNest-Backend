package com.technest_api.module.post.mapper;

import com.technest_api.module.post.dto.response.PostFullResponse;
import com.technest_api.module.post.dto.response.PostSummaryResponse;
import com.technest_api.module.post.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostFullResponse toFullDto(Post post);

    List<PostFullResponse> toFullDtoList(List<Post> posts);

    PostSummaryResponse toSummaryDto(Post post);

    List<PostSummaryResponse> toSummaryDtoList(List<Post> posts);
}
