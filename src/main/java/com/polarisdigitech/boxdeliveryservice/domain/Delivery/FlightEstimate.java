package com.polarisdigitech.boxdeliveryservice.domain.Delivery;

import java.time.Duration;

public record FlightEstimate(String effectiveSpeedKmh, Duration estimatedTravelTime) {
}
