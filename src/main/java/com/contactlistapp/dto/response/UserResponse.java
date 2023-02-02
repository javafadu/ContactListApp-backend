package com.contactlistapp.dto.response;

import com.contactlistapp.domain.Role;
import com.contactlistapp.domain.User;
import com.contactlistapp.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime registerDate;
    private Set<String> roles;

    // Converting Set<Role> roles in DB to Set<String> roles as dto
    public void setRoles(Set<Role> roles) {
        Set<String> rolesStr = new HashSet<>();

        for (Role r : roles
        ) {
            if (r.getName().equals(RoleType.ROLE_CUSTOMER)) {
                rolesStr.add("Customer");
            } else if (r.getName().equals(RoleType.ROLE_MANAGER)) {
                rolesStr.add("Manager");
            } else if (r.getName().equals(RoleType.ROLE_ADMIN)) {
                rolesStr.add("Admin");
            } else {
                rolesStr.add("Basic");
            }
        }
        this.roles = rolesStr;
    }


    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.registerDate = user.getRegisterDate();
        this.roles = getRoles();
        //  Set<Role> convert to Set<String> with above method
    }
}
