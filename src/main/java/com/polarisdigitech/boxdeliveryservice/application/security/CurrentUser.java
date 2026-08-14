package com.polarisdigitech.boxdeliveryservice.application.security;

import java.util.UUID;

public interface CurrentUser {
    UUID getId();
    String getUsername();
    boolean isAuthenticated();
}
