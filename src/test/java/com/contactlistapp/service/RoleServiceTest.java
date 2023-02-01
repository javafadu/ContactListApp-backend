package com.contactlistapp.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void existsByName() {

        Role role1 = new Role();
        role1.setId(1);
        role1.setName(RoleType.ROLE_BASIC);

        when(roleService.existsByName(role1.getName())).thenReturn(true);

        Boolean result = roleService.existsByName(role1.getName());
        assertEquals(result, true);


    }

    @Test
    void findByName() {

        Role role1 = new Role();
        role1.setId(1);
        role1.setName(RoleType.ROLE_BASIC);

        when(roleRepository.findByName(RoleType.ROLE_BASIC)).thenReturn(Optional.of(role1));

        Role checkRole1 = roleService.findByName(RoleType.ROLE_BASIC);

        assertAll(
                () -> assertEquals(checkRole1.getId(), 1),
                () -> assertEquals(checkRole1.toString(), "ROLE_BASIC"),
                () -> assertEquals(checkRole1.getName().name(), "ROLE_BASIC")

        );
    }

    @Test
    void saveRole() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName(RoleType.ROLE_BASIC);

        when(roleRepository.save(role1)).thenReturn(role1);
        roleService.saveRole(role1);
        ;
        verify(roleRepository, times(1)).save(role1);
    }

    @Test
    void addRoles() {
        List<String> existRolesList = new ArrayList<>();

        Role role1 = new Role(1, RoleType.ROLE_BASIC);
        Role role2 = new Role(2, RoleType.ROLE_ADMIN);

        existRolesList.add(role2.getName().toString());

        RoleType roleType1 = RoleType.values()[0];
        RoleType roleType2 = RoleType.values()[1];

        when(roleRepository.existsByName(roleType1)).thenReturn(false);
        roleService.addRoles();
        verify(roleRepository, times(1)).existsByName(roleType1);

        when(roleRepository.existsByName(roleType2)).thenReturn(true);
        roleService.addRoles();
        assertEquals(existRolesList.size(), 1);
    }
}