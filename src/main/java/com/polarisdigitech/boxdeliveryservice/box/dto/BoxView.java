package com.polarisdigitech.boxdeliveryservice.box.dto;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;

import java.util.UUID;

public record BoxView(UUID id,
                      String txRef,
                      double weightLimitGrams,
                      double batteryPercentage,
                      String state,
                      double currentWeightGrams){
    public static BoxView from(Box box) {
        return new BoxView(
                box.getId().getValue(),
                box.getTxRef().getValue(),
                box.getWeightLimit().getValue(),
                box.getBatteryLevel().getPercentage(),
                box.getState().name(),
                box.getTotalItemWeight().getGrams()
        );
    }

}
