package com.polarisdigitech.boxdeliveryservice.delivery.domainservices;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.FlightEstimate;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;

import java.time.Duration;
import java.time.Instant;

public final class FlightEstimator {
    public FlightEstimator() {
    }

    public static Result<FlightEstimate, DomainError> calculateEstimatedDeliveryTime(Instant startTime, double distance, double boxSpeed, double itemTotalWeight) {
        if (startTime == null) {
            return Result.failure(ValidationError.of("startTime", "startTime must not be empty or null"));
        }
        if (distance == 0) {
            return Result.failure(ValidationError.of("createdBy", "createBy must not be empty"));
        }
        if (boxSpeed <= 0 || boxSpeed > BoxConstants.MAX_SPEED) {
            return Result.failure(ValidationError.of("boxSpeed", "box speed can not be greater than the max speed"));
        }
        double itemTotalWeightInKilogram = itemTotalWeight / 1000;

        double payloadRatio = itemTotalWeightInKilogram / BoxConstants.MAX_PAYLOAD;
        double speedReduction = payloadRatio * BoxConstants.MAX_SPEED_REDUCTION;
        double effectiveSpeed = BoxConstants.MAX_SPEED * (1 - speedReduction);
        double travelTimeInHour = distance / effectiveSpeed;
        Duration travelTimeInDuration = Duration.ofSeconds((long) (travelTimeInHour * 3600));
        return Result.success(new FlightEstimate(effectiveSpeed + " km/h", travelTimeInDuration ));
    }
}
