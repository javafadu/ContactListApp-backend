package com.contactlistapp.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.dto.mapper.UserMapper;
import com.contactlistapp.dto.request.UserCreateRequest;
import com.contactlistapp.dto.request.UserRegisterRequest;
import com.contactlistapp.dto.request.UserUpdateRequest;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import com.contactlistapp.exception.ConflictException;
import com.contactlistapp.exception.ResourceNotFoundException;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.RoleRepository;
import com.contactlistapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private RoleService roleService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    // 1- ADD ROLES from ENUMS to DB
    public List<String> addRoles() {
        // create a blank list to be added already exist roles in DB
        List<String> existRolesList = new ArrayList<>();

        for (RoleType each : RoleType.values()
        ) {
            if (!roleService.existsByName(each)) {
                Role role = new Role();
                role.setName(each);
                roleService.saveRole(role);
            } else {
                existRolesList.add(each.name());
            }
        }

        return existRolesList;
    }


    // 2- REGISTER a USER (UserJWTController)
    public UserRegisterResponse register(UserRegisterRequest registerRequest) {
        // Check1: e-mail if exist or not
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException(String.format(ErrorMessage.
                    EMAIL_ALREADY_EXIST, registerRequest.getEmail()));
        }
        // keep the encoded password in DB
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Check2: First registration role of user is Member, so it should be added into the tbl_roles
        // before registration and assign it to role of the registering user
        Role role = roleService.findByName(RoleType.ROLE_BASIC);;
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Creation Date should be now
        LocalDateTime today = LocalDateTime.now();
        User user = new User();

        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setRegisterDate(today);
        user.setRoles(roles);
        userRepository.save(user);

        // return the registered user as response
        User registeredUser = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, registerRequest.getEmail())));

        return userMapper.userToUserRegisterResponse(registeredUser);
    }
    // 3- Find a USER with ID
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                com.contactlistapp.exception.message.ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)
        ));
        return userMapper.userToUserResponse(user);
    }
    // 4- Get All Users or Searched Users by Page by ADMIN
    public Page<UserResponse> getUsersByPage(String q, Pageable pageable) {

        if (!q.isEmpty()) {
            return userRepository.getFilteredUsersWithQ(q.toLowerCase(), pageable);
        } else {
            return userRepository.getAllUsersByPage(pageable);

        }

    }
    // 5- Update a user with ID by ADMIN
    public UserResponse userUpdate(Long id, UserUpdateRequest userUpdateRequest) {
        // Check1: control if the user exist or not with the requsted id
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        Boolean emailExists = userRepository.existsByEmail(userUpdateRequest.getEmail());

        // Check4: if the new email is belongs to another user in db
        if (emailExists && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST, user.getEmail()));
        }

        user.setId(id);
        user.setEmail(userUpdateRequest.getEmail());
        user.setRoles(userUpdateRequest.getRoles());
        if (userUpdateRequest.getRegisterDate() != null) {
            user.setRegisterDate(userUpdateRequest.getRegisterDate());
        }
        if (userUpdateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }
        userRepository.save(user);

        UserResponse userResponse = userMapper.userToUserResponse(user);
        return userResponse;
    }

    // 6- Delete a user with ID by ADMIN
    public UserResponse deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        userRepository.delete(user);
        return userMapper.userToUserResponse(user);
    }

    public UserRegisterResponse userCreate(UserCreateRequest userCreateRequest) {
        // Check1: e-mail if exist or not
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new RuntimeException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST, userCreateRequest.getEmail()));
        }
        // encode the string password as crypted


        // Creation Date should be now
        LocalDateTime today = LocalDateTime.now();

        User user = new User();

        user.setEmail(userCreateRequest.getEmail());
        user.setPassword(encodePassword(userCreateRequest.getPassword()));
        user.setRegisterDate(today);
        user.setRoles(userCreateRequest.getRoles());

        userRepository.save(user);

        // to get created user (including userId)
        User registeredUser = userRepository.findByEmail(userCreateRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, userCreateRequest.getEmail())));

        return userMapper.userToUserRegisterResponse(registeredUser);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
