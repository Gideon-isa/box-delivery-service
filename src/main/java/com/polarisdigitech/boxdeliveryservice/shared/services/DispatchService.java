package com.polarisdigitech.boxdeliveryservice.shared.services;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.FlightEstimate;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.FlightRoundTrip;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

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
