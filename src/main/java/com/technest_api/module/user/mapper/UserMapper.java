package com.technest_api.module.user.mapper;

import com.technest_api.module.user.dto.UserResponseDto;
import com.technest_api.module.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> users);
}
