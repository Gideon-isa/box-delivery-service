package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.CreateBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.LoadBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.LoadBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.LoadBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveredItemCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DispatchBoxCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.EstimateFlightCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.request.DispatchDeliveryRequest;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DeliveredItemResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DispatchCreatedResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.EstimateFlightResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.request.EstimateFlightRequest;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DeliveredItemUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DispatchBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.EstimateFlightUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.polarisdigitech.boxdeliveryservice.controllers.ErrorResponse.toErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final EstimateFlightUseCase estimateFlightUseCase;
    private final DispatchBoxUseCase dispatchBoxUseCase;
    private final DeliveredItemUseCase deliveredItemUseCase;

    @PostMapping
    public ResponseEntity<?> startFlight(@Valid @RequestBody DispatchDeliveryRequest request,
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

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getFlight(@PathVariable UUID boxId) {
//        Result<LoadBoxResponse, DomainError> result =
//                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
//        if (result.isFailure()) {
//            return toErrorResponse(result.getError());
//        }
//        return ResponseEntity.ok(result.getValue());
//    }


    @GetMapping("/{id}/delivered")
    public ResponseEntity<?> getFlight(@PathVariable UUID id) {
        Result<DeliveredItemResponse, DomainError> result =
                deliveredItemUseCase.execute(new DeliveredItemCommand(id));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @PostMapping("/flight-estimate")
    public ResponseEntity<?> estimateFlight(@Valid @RequestBody EstimateFlightRequest request) {
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

//    @PostMapping("/{id}/return")
//    public ResponseEntity<?> returnBox(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
//        Result<LoadBoxResponse, DomainError> result =
//                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
//        if (result.isFailure()) {
//            return toErrorResponse(result.getError());
//        }
//        return ResponseEntity.ok(result.getValue());
//    }
}
