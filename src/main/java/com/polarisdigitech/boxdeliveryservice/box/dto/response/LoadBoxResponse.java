package com.polarisdigitech.boxdeliveryservice.box.dto.response;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;

import java.util.UUID;

public final record LoadBoxResponse(UUID id,
                                    String txRef,
                                    double weightLimitGrams,
                                    double batteryPercentage,
                                    String state,
                                    double currentWeightGrams,
                                    boolean isLoaded

) {
    public static LoadBoxResponse from(Box view) {
        return new LoadBoxResponse(
                view.getId().getValue(),
                view.getTxRef().getValue(),
                view.getWeightLimit().getValue(),
                view.getBatteryLevel().getPercentage(),
                view.getState().name(),
                view.getTotalItemWeight().getGrams(),
                true

        );
    }
}
