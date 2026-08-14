package com.polarisdigitech.boxdeliveryservice.presentation.box.response;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;

import java.util.UUID;

public record BoxResponse(UUID id,
                          String txRef,
                          double weightLimitGrams,
                          double batteryPercentage,
                          String state,
                          double currentWeightGrams) {

    public static BoxResponse from(BoxView view) {
        return new BoxResponse(
                view.id(),
                view.txRef(),
                view.weightLimitGrams(),
                view.batteryPercentage(),
                view.state(),
                view.currentWeightGrams()
        );
    }
}
