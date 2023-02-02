package com.contactlistapp.dto.mapper;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    @Test
    void userToUserRegisterResponse() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        User user = new User(1L, "Name Surname", "mail@mail.com", "12345", today, roles);

        userRegisterResponse.setId(user.getId());
        userRegisterResponse.setName(user.getName());
        userRegisterResponse.setEmail(user.getEmail());
        userRegisterResponse.setRegisterDate(user.getRegisterDate());

        UserRegisterResponse registeredUser = userMapper.userToUserRegisterResponse(user);
        assertEquals(registeredUser.getName(), user.getName());


    }

    @Test
    void userToUserRegisterResponseWithNullUser() {

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        User user = null;

        UserRegisterResponse registeredUser = userMapper.userToUserRegisterResponse(user);
        assertNull(registeredUser);

    }

    @Test
    void userToUserResponse() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);

        User user = new User(1L, "Name Surname", "mail@mail.com", "12345", today, roles);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getName());
        userResponse.setRoles(user.getRoles());
        userResponse.setName(user.getName());

        UserResponse mappedUser = userMapper.userToUserResponse(user);
        assertEquals(mappedUser.getName(), user.getName());


    }

    @Test
    void userToUserResponseWithNullUser() {

        UserResponse userResponse = new UserResponse();
        User user = null;

        UserResponse respUser = userMapper.userToUserResponse(user);
        assertNull(respUser);

    }

}