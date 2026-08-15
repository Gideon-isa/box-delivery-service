package com.polarisdigitech.boxdeliveryservice.box.dto;

public record CreateBoxCommand(String txRef, double weightLimitGrams, double batteryPercentage) {
}
