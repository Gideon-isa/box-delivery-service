package com.polarisdigitech.boxdeliveryservice.box.dto.response;

public record ReturnBoxResponse(
        String message, Boolean isTurning
) {
    public static ReturnBoxResponse to(String message) {
        return new ReturnBoxResponse(message, true);
    }
}
