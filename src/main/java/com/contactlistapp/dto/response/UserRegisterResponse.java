package com.contactlistapp.dto.response;

import com.contactlistapp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime registerDate;
    private Set<Role> roles;

}
