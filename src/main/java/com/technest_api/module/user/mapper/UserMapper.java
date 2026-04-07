package com.technest_api.module.user.mapper;

import com.technest_api.module.user.dto.response.UserResponse;
import com.technest_api.module.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);

    List<UserResponse> toDtoList(List<User> users);
}
