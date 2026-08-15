package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.CreateBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.LoadBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.BoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.LoadBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.CreateBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.box.usecases.LoadBoxUseCase;
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

    private final CreateBoxUseCase createBoxUseCase;
    private final LoadBoxUseCase loadBoxUseCase;

    @PostMapping
    public ResponseEntity<?> startFlight(@Valid @RequestBody CreateBoxRequest request,
                                       UriComponentsBuilder uriBuilder) {
        CreateBoxCommand command = new CreateBoxCommand(
                request.txRef(), request.weightLimitGrams(), request.batteryPercentage());


        Result<BoxView, DomainError> result = createBoxUseCase.execute(command);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        BoxView created = result.getValue();
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/boxes/{id}").build(created.id()))
                .body(BoxResponse.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlight(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
        Result<LoadBoxResponse, DomainError> result =
                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @PostMapping("/flight-estimate")
    public ResponseEntity<?> estimateFlight(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
        Result<LoadBoxResponse, DomainError> result =
                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnBox(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
        Result<LoadBoxResponse, DomainError> result =
                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }
}
