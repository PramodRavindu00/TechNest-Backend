package com.technest_api.module.post.mapper;

import com.technest_api.module.post.dto.PostResponseDto;
import com.technest_api.module.post.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponseDto toDto(Post post);

    List<PostResponseDto> toDtoList(List<Post> posts);
}
