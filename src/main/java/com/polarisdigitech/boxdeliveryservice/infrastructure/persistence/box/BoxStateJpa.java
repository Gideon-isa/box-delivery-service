package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.box;

public enum BoxStateJpa {
    IDLE,
    LOADING,
    LOADED,
    DELIVERING,
    DELIVERED,
    RETURNING
}
