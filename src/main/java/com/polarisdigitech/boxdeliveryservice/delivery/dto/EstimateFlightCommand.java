package com.polarisdigitech.boxdeliveryservice.delivery.dto;

import java.util.UUID;

public final record EstimateFlightCommand(
        double currentLatitude,
        double currentLongitude,
        double destinationLatitude,
        double destinationLongitude,
        double speed,
        double itemTotalWeight) { }
