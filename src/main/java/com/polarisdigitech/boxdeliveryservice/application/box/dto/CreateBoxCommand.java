package com.polarisdigitech.boxdeliveryservice.application.box.dto;

import com.polarisdigitech.boxdeliveryservice.domain.item.Item;

import java.util.List;

public record CreateBoxCommand(String txRef, double weightLimitGrams, double batteryPercentage) {
}
