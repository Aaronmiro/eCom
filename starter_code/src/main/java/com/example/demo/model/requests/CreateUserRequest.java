package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateUserRequest {

    @NotNull(message = "Username cannot be missing or empty")
    @Size(min=2, message="Username must not be less than 2 characters")
    @JsonProperty
    private String username;

    @NotNull(message = "Password is a required field")
    @Size(min=8, max=16, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    @JsonProperty
    private String password;

    @NotNull(message = "cannot be missing or empty")
    @JsonProperty
    private String confirmPassword;
}
