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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.polarisdigitech.boxdeliveryservice.controllers.ErrorResponse.toErrorResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemUseCase getItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final GetAvailableItemsUseCase availableItemsUseCase;

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody CreateItemRequest request, UriComponentsBuilder uriBuilder) {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable UUID id) {
        Result<ItemView, DomainError> result = getItemUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(ItemResponse.from(result.getValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable UUID id) {
        Result<Boolean, DomainError> result = deleteItemUseCase.execute(id);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available-items")
    public ResponseEntity<?> getAvailableItems() {
        Result<List<ItemView>, DomainError> result = availableItemsUseCase.execute();
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }
}
