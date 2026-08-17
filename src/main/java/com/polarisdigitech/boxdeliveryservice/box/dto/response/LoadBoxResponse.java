package com.polarisdigitech.boxdeliveryservice.box.dto.response;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxState;
import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;

import java.util.UUID;

public final record LoadBoxResponse(UUID id,
                                    String txRef,
                                    double weightLimitGrams,
                                    double batteryPercentage,
                                    String state,
                                    double currentWeightGrams,
                                    boolean isLoaded,
                                    String message

) {
    public static LoadBoxResponse from(Box view) {
        String message = "Box is loaded with item(s). State has moved to %s".formatted(BoxState.LOADED.toString());
        return new LoadBoxResponse(
                view.getId().getValue(),
                view.getTxRef().getValue(),
                view.getWeightLimit().getValue(),
                view.getBatteryLevel().getPercentage(),
                view.getState().name(),
                view.getTotalItemWeight().getGrams(),
                true,
                message

        );
    }
}
