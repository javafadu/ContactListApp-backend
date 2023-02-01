package com.contactlistapp.controller;

import com.contactlistapp.dto.request.LoginRequest;
import com.contactlistapp.dto.request.UserRegisterRequest;
import com.contactlistapp.dto.response.LoginResponse;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.security.jwt.JwtUtils;
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
    private AuthenticationManager authManager;
    private JwtUtils jwtUtils;


    // 1- ADD ROLES from enum to DB, before registering a user.
    // endpoint: [{server_url}/addroles
    @GetMapping("/addroles")
    public ResponseEntity<Map<String,String>> addRoles() {

        List<String> existRolesList = userService.addRoles();
        Map<String, String> map = new HashMap<>();
        map.put("message", "All roles have been inserted into DB");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
    // 2- Register a User
    // endpoint: [{server_url}/register
    /*

    {
    "email": "admin@mail.com",
    "password": "12345"
    }
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest){
        UserRegisterResponse userRegisterResponse = userService.register(registerRequest);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.CREATED);
    }
    /* 3- Login
     endpoint: [{server_url}/login
    json Body
    {
    "email": "walter@mail.com",
    "password": "12345"
    }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        // STEP1 : get username and password and authenticate
        // (using AuthenticationManager in WebSecurityConfig)
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authManager.authenticate(authToken);

        // STEP2 : no exception in Step2, means successfully login, generate Jwt Token
        String token = jwtUtils.generateJwtToken(authentication);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
