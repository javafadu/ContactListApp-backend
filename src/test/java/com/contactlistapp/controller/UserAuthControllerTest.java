package com.contactlistapp.controller;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.dto.request.UserCreateRequest;
import com.contactlistapp.dto.request.UserUpdateRequest;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import com.contactlistapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthControllerTest {

    @InjectMocks
    private UserAuthController userAuthController;

    @Mock
    private UserService userService;


    private static Pageable pageable = PageRequest.of(0, 20, Sort.by("registerDate").descending());


    @Test
    void getAuthUser() {

        LocalDateTime today = LocalDateTime.now();

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail1@mail.com");
        user.setRegisterDate(today);
        user.setRoles(roles);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn((Long) 1L);

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<UserResponse> res = userAuthController.getAuthUser(request);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody().getId(), 1L);

    }

    @Test
    void getAllUsersByPage() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);

        roles.add(role);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");


        UserResponse user1 = new UserResponse(1L,"Name Surname" , "mail1@mail.com", today, rolesStr);
        UserResponse user2 = new UserResponse(2L, "Name2 Surname2", "mail2@mail.com", today, rolesStr);
        UserResponse user3 = new UserResponse(3L,"Name3 Surname3", "mail3@mail.com", today, rolesStr);
        List<UserResponse> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), users.size());
        final Page<UserResponse> pageUsers = new PageImpl<>(users.subList(start, end), pageable, users.size());

        when(userService.getUsersByPage("a", pageable)).thenReturn(pageUsers);
        ResponseEntity<Page<UserResponse>> res = userAuthController.getAllUsersByPage("a", 0, 20, "registerDate", Sort.Direction.DESC);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody().getTotalElements(), 3);

    }

    @Test
    void getUserById() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserResponse user1 = new UserResponse(1L,"Name Surname" ,"mail1@mail.com", today, rolesStr);

        UserResponse user2 = new UserResponse(2L,"Name2 Surname2", "mail2@mail.com", today, rolesStr);
        List<UserResponse> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(user1);
        ResponseEntity<UserResponse> res = userAuthController.getUserById(userId);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(userId, res.getBody().getId());

    }

    @Test
    void updateUser() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserResponse user1 = new UserResponse(1L,"Name Surname", "mail1@mail.com", today, rolesStr);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Name Surname Updated");
        updateRequest.setEmail("mail1@mail.com");
        updateRequest.setPassword("12345");
        updateRequest.setRegisterDate(today);

        Long userId = 1L;

        when(userService.userUpdate(userId, updateRequest)).thenReturn(user1);

        ResponseEntity<UserResponse> res = userAuthController.updateUser(userId, updateRequest);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(HttpStatus.OK, res.getStatusCode()),
                () -> assertEquals(1L, res.getBody().getId())
        );

    }

    @Test
    void deleteUser() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserResponse user1 = new UserResponse(1L, "Name Surname", "mail1@mail.com", today, rolesStr);

        when(userService.deleteUser(user1.getId())).thenReturn(user1);

        ResponseEntity<UserResponse> res = userAuthController.deleteUser(user1.getId());

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(HttpStatus.OK, res.getStatusCode()),
                () -> assertEquals(1L, res.getBody().getId())
        );

    }

    @Test
    void register() {
        LocalDateTime today = LocalDateTime.now();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserRegisterResponse registeredUser = new UserRegisterResponse();
        registeredUser.setId(1L);
        registeredUser.setName("Name Surname");
        registeredUser.setEmail("mail1@mail.com");
        registeredUser.setRegisterDate(today);
        registeredUser.setRoles(roles);

        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setName("Name Surname");
        createRequest.setEmail("mail1@mail.com");
        createRequest.setPassword("12345");
        createRequest.setRoles(roles);

        when(userService.userCreate(createRequest)).thenReturn(registeredUser);

        ResponseEntity<UserRegisterResponse> res = userAuthController.register(createRequest);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(HttpStatus.CREATED, res.getStatusCode()),
                () -> assertEquals(1L, res.getBody().getId())
        );


    }
}