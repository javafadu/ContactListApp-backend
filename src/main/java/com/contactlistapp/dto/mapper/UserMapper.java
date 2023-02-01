package com.contactlistapp.dto.mapper;

import com.contactlistapp.domain.User;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRegisterResponse userToUserRegisterResponse(User user);
    UserResponse userToUserResponse(User user);

}
