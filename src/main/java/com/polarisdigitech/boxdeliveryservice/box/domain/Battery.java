package com.polarisdigitech.boxdeliveryservice.box.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

public final class Battery implements ValueObject {
    public static final byte MIN_LOADING_THRESHOLD = 25;
    private static final byte MAX_CAPACITY_LEVEL = 100;
    private static final double BATTERY_CONSUMPTION_PERCENT_PER_KILOMETER = 5.0;
    private double batteryLevelPercentage;

    private Battery(double batteryLevelPercentage) {
        this.batteryLevelPercentage = batteryLevelPercentage;
    }

    public static Result<Battery, DomainError> of(double batteryLevelPercentage) {
        if (batteryLevelPercentage < 0 || batteryLevelPercentage > 100) {
            return Result.failure(ValidationError.of("batteryCapacity", "Battery capacity must be between 0 and 100"));
        }
        return Result.success(new Battery(batteryLevelPercentage));
    }

    public double getBatteryConsumptionPercentPerKilometer() {
        return BATTERY_CONSUMPTION_PERCENT_PER_KILOMETER;
    }
    public double getPercentage() {
        return batteryLevelPercentage;
    }

    protected void setPercentage(byte batteryLevelPercentage) {
        this.batteryLevelPercentage = batteryLevelPercentage;
    }

    public boolean isBelowLoadingThreshold() {
        return batteryLevelPercentage < MIN_LOADING_THRESHOLD;
    }
}
