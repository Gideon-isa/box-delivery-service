package com.polarisdigitech.boxdeliveryservice.db.box;

public enum BoxStateJpa {
    IDLE,
    LOADING,
    LOADED,
    DELIVERING,
    DELIVERED,
    RETURNING
}
