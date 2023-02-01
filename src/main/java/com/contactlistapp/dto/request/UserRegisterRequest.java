package com.contactlistapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {


    private String name;

    @Email()
    @Size(min = 7, max = 180, message = "The email '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide an email address")
    private String email;

    @NotNull(message = "Please provide a valid password")
    private String password;




}
