package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.dto.CreateItemCommand;
import com.polarisdigitech.boxdeliveryservice.item.dto.request.CreateItemRequest;
import com.polarisdigitech.boxdeliveryservice.item.dto.response.ItemResponse;
import com.polarisdigitech.boxdeliveryservice.item.usecases.CreateItemUseCase;
import com.polarisdigitech.boxdeliveryservice.item.usecases.DeleteItemUseCase;
import com.polarisdigitech.boxdeliveryservice.item.usecases.GetAvailableItemsUseCase;
import com.polarisdigitech.boxdeliveryservice.item.usecases.GetItemUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemUseCase getItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final GetAvailableItemsUseCase availableItemsUseCase;

    @Operation(
            summary = "Create a new item",
            description = "Creates a new item for delivery."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_ITEM_CREATE')")
    public ResponseEntity<?> createItem(@Validated({Default.class}) @Valid @RequestBody CreateItemRequest request, UriComponentsBuilder uriBuilder) {
        CreateItemCommand command = new CreateItemCommand(
                request.name(), request.weight(), request.code());

        Result<ItemView, DomainError> result = createItemUseCase.execute(command);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        ItemView created = result.getValue();
        return ResponseEntity
                .created(uriBuilder.path("api/v1/boxes/{id}").build(created.id()))
                .body(ItemResponse.from(created));
    }

    @Operation(
            summary = "Retrieves a new item",
            description = "Returns a items that is available ."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_ITEM_READ')")
    public ResponseEntity<?> getItem(@PathVariable UUID id) {
        Result<ItemView, DomainError> result = getItemUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(ItemResponse.from(result.getValue()));
    }

    @Operation(
            summary = "Deletes an item",
            description = "Delete an item."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_ITEM_DELETE')")
    public ResponseEntity<?> deleteItem(@PathVariable UUID id) {
        Result<Boolean, DomainError> result = deleteItemUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Retrieves available items not yet loaded",
            description = "Retrieves items that are not yet loaded."
    )
    @GetMapping("/available-items")
    @PreAuthorize("hasAuthority('PERMISSION_ITEM_READ')")
    public ResponseEntity<?> getAvailableItems() {
        Result<List<ItemView>, DomainError> result = availableItemsUseCase.execute();
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }
}
