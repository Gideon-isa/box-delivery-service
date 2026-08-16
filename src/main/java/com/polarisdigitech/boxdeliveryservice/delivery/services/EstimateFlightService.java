package com.polarisdigitech.boxdeliveryservice.delivery.services;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Coordinates;
import com.polarisdigitech.boxdeliveryservice.delivery.domainservices.FlightEstimator;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.EstimateFlightCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.EstimateFlightResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.EstimateFlightUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EstimateFlightService implements EstimateFlightUseCase {

    @Transactional
    @Override
    public Result<EstimateFlightResponse, DomainError> execute(EstimateFlightCommand command) {

        Result<Coordinates, DomainError> locationResult = Coordinates.of(command.currentLatitude(), command.currentLongitude());
        if (locationResult.isFailure()) {
            return Result.failure(locationResult.getError());
        }

        Result<Coordinates, DomainError> destionationResult = Coordinates.of(command.destinationLatitude(), command.destinationLongitude());
        if (destionationResult.isFailure()) {
            return Result.failure(destionationResult.getError());
        }

        var currentLocation = locationResult.getValue();
        var destination = destionationResult.getValue();

        var distance = currentLocation.distanceToKm(destination);

        var estimatedResult = FlightEstimator.calculateEstimatedDeliveryTime(
                Instant.now(), distance, command.speed(), command.itemTotalWeight());

        if (estimatedResult.isFailure()) {
            return Result.failure(estimatedResult.getError());
        }
        return Result.success(EstimateFlightResponse.from(estimatedResult.getValue()));
    }
}
