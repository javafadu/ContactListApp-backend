package com.contactlistapp.controller;


import com.contactlistapp.dto.request.UserCreateRequest;
import com.contactlistapp.dto.request.UserUpdateRequest;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import com.contactlistapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserAuthController {

    private UserService userService;

    // 1- GET Auth User Information
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BASIC')")
    public ResponseEntity<UserResponse> getAuthUser(HttpServletRequest httpServletRequest) {
        Long id = (Long) httpServletRequest.getAttribute("id");
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // 2- GET All Users with Paging by ADMIN
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsersByPage(
            @RequestParam(required = false, value = "q", defaultValue = "") String q,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "20") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "registerDate") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<UserResponse> usersWithPage = userService.getUsersByPage(q, pageable);
        return ResponseEntity.ok(usersWithPage);
    }

    // 3- GET a USER with ID by ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // 4- Update A User by ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@Valid @PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.userUpdate(id, userUpdateRequest);
        return ResponseEntity.ok(userResponse);
    }

    // 5- Delete A User by ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        UserResponse userResponse = userService.deleteUser(id);
        return ResponseEntity.ok(userResponse);
    }

    // 6- Create a user by ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserCreateRequest userCreateRequest){

        UserRegisterResponse userRegisterResponse = userService.userCreate(userCreateRequest);
        return new ResponseEntity<>(userRegisterResponse, HttpStatus.CREATED);
    }


}
