package com.contactlistapp.service;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import com.contactlistapp.exception.ResourceNotFoundException;
import com.contactlistapp.exception.message.ErrorMessage;
import com.contactlistapp.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {

    private RoleRepository roleRepository;

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
