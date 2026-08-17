package com.polarisdigitech.boxdeliveryservice.auth;

import java.util.UUID;

public record SignupResponse(UUID userId, String username, String email, String defaultRole) {
}
