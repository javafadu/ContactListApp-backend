package com.contactlistapp.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.exception.ResourceNotFoundException;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RoleService {

    private RoleRepository roleRepository;


    public List<String> addRoles() {
        // create a blank list to be added already exist roles in DB
        List<String> existRolesList = new ArrayList<>();

        for (RoleType each : RoleType.values()
        ) {
            if (!roleRepository.existsByName(each)) {
                Role role = new Role();
                role.setName(each);
                roleRepository.save(role);
            } else {
                existRolesList.add(each.name());
            }
        }

        return existRolesList;
    }


    public Boolean existsByName(RoleType name) {
        return roleRepository.existsByName(name);
    }

    public Role findByName(RoleType name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessage.ROLE_NOT_FOUND_MESSAGE, name)
                ));
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }



}
