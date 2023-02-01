package com.contactlistapp.domain;

import com.contactlistapp.domain.enums.RoleType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void passwordAndRoleTest() {


        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        role1.setName(RoleType.ROLE_BASIC);
        role1.setId(1);
        roles.add(role1);

        User user = new User();
        user.setPassword("12345");
        user.setRoles(roles);

        assertEquals(user.getPassword(),"12345");
        assertEquals(user.getRoles().isEmpty(),false);
    }


}