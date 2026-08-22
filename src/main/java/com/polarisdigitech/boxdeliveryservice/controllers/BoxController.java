package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.ReturnBoxResponse;
import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.LoadBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.*;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.CreateBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.request.LoadBoxRequest;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.BatteryLevelResponse;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.BoxResponse;
import com.polarisdigitech.boxdeliveryservice.item.dto.response.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.polarisdigitech.boxdeliveryservice.exception.ErrorResponse.toErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boxes")
public class BoxController {

    private final CreateBoxUseCase createBoxUseCase;
    private final LoadBoxUseCase loadBoxUseCase;
    private final GetAvailableBoxesUseCase getAvailableBoxesUseCase;
    private final GetBatteryLevelUseCase getBatteryLevelUseCase;
    private final GetLoadedItemUseCase getLoadedItemsUseCase;
    private final ReturnBoxUseCase returnBoxUseCase;
    private final ReturnedBoxUseCase returnedBoxUseCase;


    @Operation(
            summary = "Create a new box",
            description = "Creates a new box for delivery."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_BOX_CREATE')")
    public ResponseEntity<?> createBox(@Validated({Default.class}) @Valid @RequestBody CreateBoxRequest request,
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

    @Operation(
            summary = "Loads box with items.",
            description = "Loading of items on into the box."
    )
    @PostMapping("/{boxId}/load")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_LOAD')")
    public ResponseEntity<?> loadBox(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
        Result<LoadBoxResponse, DomainError> result =
                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @Operation(
            summary = "Retrieves items loaded to a box",
            description = "This returns list of items loaded onto a box."
    )
    @GetMapping("/{boxId}/items")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_READ')")
    public ResponseEntity<?> getLoadedItems(@PathVariable UUID boxId) {
        Result<List<ItemView>, DomainError> result = getLoadedItemsUseCase.execute(boxId);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        List<ItemResponse> items = result.getValue().stream().map(ItemResponse::from).toList();
        return ResponseEntity.ok(items);
    }

    @Operation(
            summary = "Retrieves IDLE boxes",
            description = "Returns boxes in the IDLE state."
    )
    @GetMapping("/available")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_READ')")
    public ResponseEntity<List<BoxResponse>> getAvailableBoxes() {
        List<BoxResponse> boxes = getAvailableBoxesUseCase
                .execute()
                .stream()
                .map(BoxResponse::from)
                .toList();
        return ResponseEntity.ok(boxes);
    }

    @Operation(
            summary = "Returns the battery percentage for box",
            description = "Returns the current battery percentage."
    )
    @GetMapping("/{boxId}/battery")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_READ')")
    public ResponseEntity<?> getBatteryLevel(@PathVariable UUID boxId) {
        Result<String, DomainError> result = getBatteryLevelUseCase.execute(boxId);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(new BatteryLevelResponse(result.getValue()));
    }

    @Operation(
            summary = "Set box on flight back to base.",
            description = "Set the box to RETURNING."
    )
    @PostMapping("/{id}/return")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_RETURN')")
    public ResponseEntity<?> returnBox(@PathVariable UUID id) {
        Result<ReturnBoxResponse, DomainError> result =
                returnBoxUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    @Operation(
            summary = "Box has returned back to base. State moved to IDLE",
            description = "When the box has successfully reached based.."
    )
    @PostMapping("/{id}/returned")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_RETURNED')")
    public ResponseEntity<?> returnedBox(@PathVariable UUID id) {
        Result<ReturnBoxResponse, DomainError> result =
                returnedBoxUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }
}


