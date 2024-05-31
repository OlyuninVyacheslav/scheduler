package com.scheduler.backend.mappers;

import com.scheduler.backend.dtos.SignUpDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}