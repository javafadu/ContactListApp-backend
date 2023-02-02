package com.contactlistapp.dto.request;


import com.contactlistapp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    private String name;

    @Email()
    @Size(min = 10, max = 180, message = "Your email '${validatedValue}' must be between {min} and {max} chars long")
    private String email;
    private String password;
    private LocalDateTime registerDate;
    private Set<Role> roles;


}
