package com.contactlistapp.security.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        String emailStr="mail@mail.com";

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);



        User user = new User();
        user.setId(1L);
        user.setName("Name Surname");
        user.setEmail("mail@mail.com");
        user.setPassword("12345");
        user.setRoles(roles);

        // below 3 row for only testing UserDetailsImpl wont be used further
        UserDetailsImpl userDetails1 = new UserDetailsImpl();
        userDetails1.setUser(user);
        userDetails1.getUser();

        Mockito.when(userRepository.findByEmail(emailStr)).thenReturn(Optional.of(user));

        assertEquals(userDetailsService.loadUserByUsername(emailStr).getUsername(),user.getEmail());
        assertEquals(userDetailsService.loadUserByUsername(emailStr).isAccountNonExpired(),true);
        assertEquals(userDetailsService.loadUserByUsername(emailStr).isAccountNonLocked(),true);
        assertEquals(userDetailsService.loadUserByUsername(emailStr).isCredentialsNonExpired(),true);
        assertEquals(userDetailsService.loadUserByUsername(emailStr).isEnabled(),true);
        assertEquals(userDetailsService.loadUserByUsername(emailStr).getPassword(),user.getPassword());
        assertEquals(userDetailsService.loadUserByUsername(emailStr).getAuthorities().size(),1);



    }

    @Test
    void loadUserByUsernameWithWrongEmail() {
        String emailStr="mail@mail.com";



        Mockito.when(userRepository.findByEmail(emailStr)).thenReturn(Optional.ofNullable(null));

        String msg = "";

        try {
            userDetailsService.loadUserByUsername(emailStr);

        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertEquals(msg, String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE,emailStr));
    }
}