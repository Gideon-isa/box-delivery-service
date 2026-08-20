package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.delivery.dto.*;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.request.DispatchDeliveryRequest;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DeliveredItemResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DispatchCreatedResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.EstimateFlightResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.request.EstimateFlightRequest;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DeliveredItemUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DispatchBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.EstimateFlightUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.GetDeliveryUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.polarisdigitech.boxdeliveryservice.exception.ErrorResponse.toErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final EstimateFlightUseCase estimateFlightUseCase;
    private final DispatchBoxUseCase dispatchBoxUseCase;
    private final DeliveredItemUseCase deliveredItemUseCase;
    private final GetDeliveryUseCase deliveryUseCase;


    @Operation(
            summary = "Dispatch box to a location. DELIVERING",
            description = "Starts a delivering process."
    )
    @PostMapping
    public ResponseEntity<?> startFlight(@Validated({Default.class}) @Valid @RequestBody DispatchDeliveryRequest request,
                                       UriComponentsBuilder uriBuilder) {
        DispatchBoxCommand command = new DispatchBoxCommand(
                request.boxId(),
                request.remoteLocationName(),
                request.currentLocation().latitude(),
                request.currentLocation().longitude(),
                request.destinationLocation().latitude(),
                request.destinationLocation().longitude(),
                request.setSpeed());

        Result<DeliveryView, DomainError> result = dispatchBoxUseCase.execute(command);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        DeliveryView created = result.getValue();
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/deliveries/{id}").build(created.id()))
                .body(DispatchCreatedResponse.from(created));
    }

    @Operation(
            summary = "Retrieves a delivery record",
            description = "Returns a delivery record."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getDelivery(@PathVariable UUID id) {
        Result<DeliveryView, DomainError> result =
                deliveryUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }


    @Operation(
            summary = "Marked the box as delivered. moves state to DELIVERED",
            description = "Box has reached the destination and marks the box as delivered."
    )
    @GetMapping("/{id}/delivered")
    public ResponseEntity<?> getFlight(@PathVariable UUID id) {
        Result<DeliveredItemResponse, DomainError> result =
                deliveredItemUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @Operation(
            summary = "Returns an estimated duration of the flight from the current position to a destination",
            description = "Returns an estimated flight time"
    )
    @PostMapping("/flight-estimate")
    public ResponseEntity<?> estimateFlight(@Validated({Default.class}) @RequestBody EstimateFlightRequest request) {
        Result<EstimateFlightResponse, DomainError> result =
                estimateFlightUseCase.execute(
                        new EstimateFlightCommand(
                                request.currentLocation().latitude(),
                                request.currentLocation().longitude(),
                                request.destinationLocation().latitude(),
                                request.destinationLocation().longitude(),
                                request.speed(),
                                request.itemTotalWeightInGrams()));

        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }


}
