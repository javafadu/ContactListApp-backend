package com.contactlistapp.dto.request;

import com.contactlistapp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    private String name;

    @Email()
    @Size(min = 7, max = 180, message = "Your email '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide email address")
    private String email;

    @NotNull(message = "Please provide password")
    private String password;

    @NotNull(message = "Please provide roles")
    private Set<Role> roles;

}
