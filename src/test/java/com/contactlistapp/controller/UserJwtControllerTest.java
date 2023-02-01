package com.contactlistapp.controller;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.dto.request.LoginRequest;
import com.contactlistapp.dto.request.UserRegisterRequest;
import com.contactlistapp.dto.response.LoginResponse;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.security.jwt.JwtUtils;
import com.contactlistapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserJwtControllerTest {

    @InjectMocks
    private UserJwtController userJwtController;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtUtils jwtUtils;

    @Test
    void register() {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setName("Name Surname");
        userRegisterRequest.setEmail("mail1@mail.com");
        userRegisterRequest.setPassword("12345");

        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setId(1L);
        userRegisterResponse.setEmail(userRegisterResponse.getEmail());
        userRegisterResponse.setRegisterDate(LocalDateTime.now());
        userRegisterResponse.setRoles(roles);


        when(userService.register(Mockito.any(UserRegisterRequest.class))).thenReturn(userRegisterResponse);

        ResponseEntity<UserRegisterResponse> res = userJwtController.register(userRegisterRequest);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertEquals(res.getBody().getId(),1);
    }

    @Test
    void authenticate() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("mail1@mail.com");
        loginRequest.setPassword("12345");

        String token = "123456789";

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authManager.authenticate(authToken);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        ResponseEntity<LoginResponse> res = userJwtController.authenticate(loginRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody().getToken(),token);
    }
}