package com.contactlistapp.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.dto.mapper.UserMapper;
import com.contactlistapp.dto.request.LoginRequest;
import com.contactlistapp.dto.request.UserCreateRequest;
import com.contactlistapp.dto.request.UserRegisterRequest;
import com.contactlistapp.dto.request.UserUpdateRequest;
import com.contactlistapp.dto.response.LoginResponse;
import com.contactlistapp.dto.response.UserRegisterResponse;
import com.contactlistapp.dto.response.UserResponse;
import com.contactlistapp.exception.ResourceNotFoundException;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.UserRepository;
import com.contactlistapp.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authManager;

    private static Pageable pageable = PageRequest.of(0, 5, Sort.by("registerDate", "DESC"));


    @Test
    void registerWithNewEmail() {

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("Name Surname", "mail1@mail.com","12345");


        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(1);
        roles.add(role);

        User user = new User();

        when(roleService.findByName(RoleType.ROLE_BASIC)).thenReturn(role);
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail1@mail.com");
        String passwordStr = "12345";
        user.setPassword(userService.encodePassword(passwordStr));
        user.setRegisterDate(LocalDateTime.now());
        user.setRoles(roles);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userRepository.findByEmail(userRegisterRequest.getEmail())).thenReturn(Optional.of(user));

        UserRegisterResponse mappedUser = new UserRegisterResponse(user.getId(), user.getName(), user.getEmail(), user.getRegisterDate(), roles);

        when(userMapper.userToUserRegisterResponse(Mockito.any(User.class))).thenReturn(mappedUser);

        UserRegisterResponse registeredUser = userService.register(userRegisterRequest);

        assertEquals(registeredUser.getEmail(), userRegisterRequest.getEmail());
        assertEquals(registeredUser.getRoles().isEmpty(), false);
        assertNotEquals(user.getPassword(), passwordStr);
        assertEquals(registeredUser.getEmail(), userRegisterRequest.getEmail());
        assertEquals(registeredUser.getRegisterDate(), user.getRegisterDate());
        assertEquals(registeredUser.getName(), user.getName());


    }

    // test registering with already saved email (get exception)
    @Test
    void registerWithExistEmail() {

        UserRegisterRequest newUserRegisterRequest = new UserRegisterRequest();
        newUserRegisterRequest.setName("Name Surname");
        newUserRegisterRequest.setEmail("mail1@mail.com");
        newUserRegisterRequest.setPassword("12345");

        when(userRepository.existsByEmail(newUserRegisterRequest.getEmail())).thenReturn(true);

        String msg = "";

        try {
            userService.register(newUserRegisterRequest);

        } catch (Exception e) {
            msg = e.getMessage();
        }
        assertEquals(msg, "Email already exist:mail1@mail.com");
    }



    @Test
    void authenticate() {
        LoginRequest loginRequest = new LoginRequest("mail1@mail.com","12345");


        String token = "123456789";

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authManager.authenticate(authToken);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        userService.authenticate(loginRequest);

        assertEquals(userService.authenticate(loginRequest).getToken(), token);

    }

    @Test
    void findByExistId() {

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);

        Role role2 = new Role();
        role2.setName(RoleType.ROLE_CUSTOMER);
        role2.setId(2);

        Role role3 = new Role();
        role3.setName(RoleType.ROLE_MANAGER);
        role3.setId(3);

        Role role4 = new Role();
        role4.setName(RoleType.ROLE_ADMIN);
        role4.setId(4);


        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        roles.add(role4);


        User user = new User();
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail@mail.com");
        user.setRoles(roles);

        LocalDateTime today = LocalDateTime.now();

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");
        rolesStr.add("Customer");
        rolesStr.add("Manager");
        rolesStr.add("Admin");

        UserResponse mappedUser = new UserResponse(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(Mockito.any(User.class))).thenReturn(mappedUser);
        mappedUser.setRoles(roles);

        UserResponse foundUser = userService.findById(user.getId());

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findByNonExistId() {

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(5);
        roles.add(role1);

        User user = new User();
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail@mail.com");
        user.setRoles(roles);

        LocalDateTime today = LocalDateTime.now();
        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserResponse mappedUser = new UserResponse(1L, "Name Surnam", "mail1@mail.com", today, rolesStr);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

                /* .thenThrow(new ResourceNotFoundException(String.format(
                com.contactlistapp.exception.message.ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, user.getId())));
                */



        String msg = "";

        try {
            userService.findById(user.getId());

        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertEquals(msg, String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,user.getId()));
    }


    @Test
    void getUsersByPage() {

        LocalDateTime today = LocalDateTime.now();
        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");

        UserResponse user1 = new UserResponse(1L, "Name Surname", "mail1@mail.com", today, rolesStr);
        UserResponse user2 = new UserResponse(2L, "Name2 Surname2", "mail2@mail.com", today, rolesStr);
        UserResponse user3 = new UserResponse(3L, "Name3 Surname3", "mail3@mail.com", today, rolesStr);


        List<UserResponse> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);


        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), users.size());
        final Page<UserResponse> pageUsers = new PageImpl<>(users.subList(start, end), pageable, users.size());

        when(userRepository.getAllUsersByPage(any())).thenReturn(pageUsers);

        userService.getUsersByPage("", pageable);
        userService.getUsersByPage("a", pageable);

        verify(userRepository, times(1)).getAllUsersByPage(pageable);
        verify(userRepository, times(1)).getFilteredUsersWithQ("a", pageable);
        verifyNoMoreInteractions(userRepository);
        assertEquals(3, pageUsers.getTotalElements());

    }

    @Test
    void userUpdate() {

        Long userId1 = 1L; // user id to be updated
        LocalDateTime today = LocalDateTime.now();

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);

        UserUpdateRequest updateUser1 = new UserUpdateRequest("Name Surname","mail1@mail.com","12345",today,roles);

        Role role2 = new Role();
        role2.setName(RoleType.ROLE_CUSTOMER);
        role2.setId(2);


        Role role3 = new Role();
        role3.setName(RoleType.ROLE_MANAGER);
        role3.setId(3);

        Role role4 = new Role();
        role4.setName(RoleType.ROLE_ADMIN);
        role4.setId(4);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");
        rolesStr.add(("Customer"));
        rolesStr.add(("Manager"));
        rolesStr.add(("Admin"));

        User user1 = new User(1L, "Name Surname", "mail1@mail.com", "12345", today, roles);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        UserResponse updatedUser1 = new UserResponse(1L, updateUser1.getName(), "mail1@mail.com", today, rolesStr);
        when(userMapper.userToUserResponse(Mockito.any(User.class))).thenReturn(updatedUser1);

        UserResponse userResponse = userService.userUpdate(userId1, updateUser1);

        assertAll(
                () -> assertEquals(updatedUser1.getId(), userResponse.getId()),
                () -> assertEquals(updatedUser1.getName(), userResponse.getName()),
                () -> assertEquals(updatedUser1.getEmail(), userResponse.getEmail()),
                () -> assertEquals(updatedUser1.getRegisterDate(), userResponse.getRegisterDate()),
                () -> assertEquals(updatedUser1.getRoles(), userResponse.getRoles()),
                () -> assertTrue(userResponse.getRoles().contains("Customer"))

        );

    }


    @Test
    void userUpdateWithExistingEmail() {
        UserUpdateRequest updateUser1 = new UserUpdateRequest();
        Long userId1 = 1L; // user id to be updated
        LocalDateTime today = LocalDateTime.now();

        updateUser1.setName("Name Surname");
        updateUser1.setEmail("mail@mail.com");
        updateUser1.setPassword("12345");

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);

        roles.add(role1);

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");


        User user1 = new User(1L, "Name Surname", "exist@mail.com", "12345", today, roles);

        when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));

        when(userRepository.existsByEmail(updateUser1.getEmail())).thenReturn(true);

        String msg = "";

        try {
            userService.userUpdate(userId1, updateUser1);

        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertEquals(msg,String.format(ErrorMessage.EMAIL_ALREADY_EXIST,user1.getEmail()));

    }

    @Test
    void userUpdateWithNonExistingId() {
        UserUpdateRequest updateUser1 = new UserUpdateRequest();
        Long userId1 = 1L; // user id to be updated
        LocalDateTime today = LocalDateTime.now();

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);

        updateUser1.setName("Name Surname");
        updateUser1.setEmail("mail@mail.com");
        updateUser1.setPassword("12345");
        updateUser1.setRoles(roles);


        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        String msg = "";

        try {
            userService.userUpdate(userId1,updateUser1);

        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertEquals(msg, String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,userId1));
    }



    @Test
    void deleteUser() {

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(5);
        roles.add(role1);

        User user1Mock = Mockito.mock(User.class);
        user1Mock.setName("Name Surname");
        user1Mock.setEmail("mail1@mail.com");
        user1Mock.setPassword("1234");
        user1Mock.setRoles(roles);
        user1Mock.setId(1L);

        User user2Mock = Mockito.mock(User.class);
        user2Mock.setName("Name2 Surname2");
        user2Mock.setEmail("mail2@mail.com");
        user2Mock.setPassword("1234");
        user2Mock.setRoles(roles);
        user2Mock.setId(2L);


        when(userRepository.findById(1L)).thenReturn(Optional.of(user1Mock));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user1Mock);


    }

    @Test
    void userCreateByAdmin() {

        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(5);
        roles.add(role);

        UserCreateRequest userCreateRequest = new UserCreateRequest("Name Surname","mail1@mail.com","12345",roles);


        User user = new User();
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail1@mail.com");
        user.setPassword(userService.encodePassword("12345"));
        user.setRegisterDate(LocalDateTime.now());
        user.setRoles(userCreateRequest.getRoles());

        Set<String> rolesStr = new HashSet<>();
        rolesStr.add("Basic");


        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userRepository.findByEmail(userCreateRequest.getEmail())).thenReturn(Optional.of(user));

        UserRegisterResponse mappedUser = new UserRegisterResponse(user.getId(), user.getName(), user.getEmail(), user.getRegisterDate(), roles);

        when(userMapper.userToUserRegisterResponse(Mockito.any(User.class))).thenReturn(mappedUser);

        UserRegisterResponse registeredUser = userService.userCreate(userCreateRequest);

        assertAll(
                ()-> assertEquals(registeredUser.getEmail(), userCreateRequest.getEmail()),
                ()-> assertEquals(registeredUser.getName(),userCreateRequest.getName()),
                ()-> assertEquals(registeredUser.getRegisterDate(),user.getRegisterDate()),
                ()-> assertEquals(registeredUser.getRoles(),userCreateRequest.getRoles()),
                ()-> assertEquals(registeredUser.getId(),mappedUser.getId())

        );

    }

    @Test
    void userCreateByAdmiWithExistEmail() {

        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setName(RoleType.ROLE_BASIC);
        role.setId(5);
        roles.add(role);

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName("Name Surname");
        userCreateRequest.setEmail("exist@mail.com");
        userCreateRequest.setPassword("12345");
        userCreateRequest.setRoles(roles);


        String existEmail = "exist@mail.com";
        when(userRepository.existsByEmail(userCreateRequest.getEmail())).thenReturn(true);
        String msg = "";

        try {
            userService.userCreate(userCreateRequest);

        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertEquals(msg, "Email already exist:" + userCreateRequest.getEmail());


    }


}