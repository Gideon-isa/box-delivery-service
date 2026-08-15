package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import java.time.Duration;

public record FlightEstimate(String effectiveSpeedKmh, Duration estimatedTravelTime) {
}
