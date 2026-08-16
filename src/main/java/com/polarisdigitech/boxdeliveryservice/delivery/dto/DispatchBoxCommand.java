package com.polarisdigitech.boxdeliveryservice.delivery.dto;

import java.util.UUID;

public record DispatchBoxCommand(
        UUID boxId,
        String remoteLocationName,
        double currentLatitude,
        double currentLongitude,
        double destinationLatitude,
        double destinationLongitude,
        double speed){ }
