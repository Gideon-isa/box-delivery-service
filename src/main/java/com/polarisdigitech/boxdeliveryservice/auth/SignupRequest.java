package com.polarisdigitech.boxdeliveryservice.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank
        String username,

        @Email @NotBlank
        String email,

        @NotBlank @Size(min = 8)
        String password
) {

}
