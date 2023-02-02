package com.contactlistapp.controller;

import com.contactlistapp.dto.request.LoginRequest;
import com.contactlistapp.dto.request.UserRegisterRequest;
import com.contactlistapp.dto.response.LoginResponse;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.security.jwt.JwtUtils;
import com.contactlistapp.service.RoleService;
import com.contactlistapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserJwtController {

    private UserService userService;


    // 1- Register a User
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest){
        UserRegisterResponse userRegisterResponse = userService.register(registerRequest);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.CREATED);
    };

    // 2- Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.authenticate(loginRequest), HttpStatus.OK);
    }
}
