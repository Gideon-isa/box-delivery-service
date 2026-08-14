package com.polarisdigitech.boxdeliveryservice.domain.box;

import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValueObject;

public final class Battery implements ValueObject {
    public static final byte MIN_LOADING_THRESHOLD = 25;
    private static final byte MAX_CAPACITY_LEVEL = 100;
    private byte batteryLevelPercentage;

    private Battery(byte batteryLevelPercentage) {
        this.batteryLevelPercentage = batteryLevelPercentage;
    }

    public static Result<Battery, DomainError> of(byte batteryLevelPercentage) {
        if (batteryLevelPercentage < 0 || batteryLevelPercentage > 100) {
            return Result.failure(ValidationError.of("batteryCapacity", "Battery capacity must be between 0 and 100"));
        }
        return Result.success(new Battery(batteryLevelPercentage));
    }

    public byte getPercentage() {
        return batteryLevelPercentage;
    }

    protected void setPercentage(byte batteryLevelPercentage) {
        this.batteryLevelPercentage = batteryLevelPercentage;
    }

    public boolean isBelowLoadingThreshold() {
        return batteryLevelPercentage < MIN_LOADING_THRESHOLD;
    }
}
