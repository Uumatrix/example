package com.umatrix.example.mapstruct;

import com.umatrix.example.dto.UserDto;
import com.umatrix.example.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

     Users toUser(UserDto userDto);

     UserDto toUserDto(Users user);
}
