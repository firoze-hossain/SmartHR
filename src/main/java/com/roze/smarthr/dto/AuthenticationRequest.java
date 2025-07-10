package com.roze.smarthr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @NotBlank(message = "Email can not be empty")
    @NotNull(message = "Email can not be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password can not be empty")
    @NotNull(message = "Password can not be null")
    private String password;
}