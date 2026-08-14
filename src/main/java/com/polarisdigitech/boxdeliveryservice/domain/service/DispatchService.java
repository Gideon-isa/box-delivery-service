package com.polarisdigitech.boxdeliveryservice.domain.service;

import com.polarisdigitech.boxdeliveryservice.domain.Delivery.Delivery;
import com.polarisdigitech.boxdeliveryservice.domain.Delivery.FlightEstimate;
import com.polarisdigitech.boxdeliveryservice.domain.Delivery.FlightRoundTrip;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValidationError;
import jdk.jshell.spi.ExecutionControl;

import java.time.Duration;
import java.time.Instant;

public final class DispatchService {
    private DispatchService() {};

    public static Result<FlightRoundTrip, DomainError> calculateEstimatedDeliveryTime(Instant startTime, double distance, double boxSpeed, double itemTotalWeight) {

        Result<FlightEstimate, DomainError> flightResult =  FlightEstimator.calculateEstimatedDeliveryTime(startTime, distance, boxSpeed, itemTotalWeight);
        if (flightResult.isFailure()) {
            return Result.failure(flightResult.getError());
        }

        // Return Flight
        Result<FlightEstimate, DomainError> returnedFlightResult =  FlightEstimator.calculateEstimatedDeliveryTime(startTime, distance, boxSpeed, BoxConstants.BOX_WEIGHT);
        if (returnedFlightResult.isFailure()) {
            return Result.failure(flightResult.getError());
        }

        Duration flightEstimate =  flightResult.getValue().estimatedTravelTime();
        Duration returnArrivalTime =  returnedFlightResult.getValue().estimatedTravelTime();

        Instant locationArrivalTime = Instant.now().plus(flightEstimate);
        Instant expectedReturnedTime = Instant.now().plus(returnArrivalTime);
        Instant departureTime = Instant.now();
        Result<FlightRoundTrip, DomainError> roundTripResult =  FlightRoundTrip.build(locationArrivalTime, expectedReturnedTime, departureTime);
        if (roundTripResult.isFailure()) {
            return Result.failure(roundTripResult.getError());
        }
        return Result.success(roundTripResult.getValue());


    }

    //Private Method


}
