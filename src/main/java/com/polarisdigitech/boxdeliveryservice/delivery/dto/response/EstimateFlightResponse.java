package com.polarisdigitech.boxdeliveryservice.delivery.dto.response;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.FlightEstimate;

import java.time.Duration;

public record EstimateFlightResponse(String effectiveSpeed, String estimatedTravelTime) {

    public static EstimateFlightResponse from(FlightEstimate flightEstimate) {

        var totalTime = String.format("%02d:%02d:%02d",
                flightEstimate.estimatedTravelTime().toHours(),
                flightEstimate.estimatedTravelTime().toMinutesPart(),
                flightEstimate.estimatedTravelTime().toSecondsPart());

        return new EstimateFlightResponse(
                flightEstimate.effectiveSpeedKmPerHr(),
                totalTime);
    }
}
