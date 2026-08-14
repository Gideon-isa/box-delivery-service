package com.polarisdigitech.boxdeliveryservice.presentation.box;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.application.box.port.in.*;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.presentation.box.request.CreateBoxRequest;
import com.polarisdigitech.boxdeliveryservice.presentation.box.request.LoadBoxRequest;
import com.polarisdigitech.boxdeliveryservice.presentation.box.response.BatteryLevelResponse;
import com.polarisdigitech.boxdeliveryservice.presentation.box.response.BoxResponse;
import com.polarisdigitech.boxdeliveryservice.presentation.item.response.ItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boxes")
public class BoxController {

    private final CreateBoxUseCase createBoxUseCase;
    private final LoadBoxUseCase loadBoxUseCase;
    private final GetAvailableBoxesUseCase getAvailableBoxesUseCase;
    private final GetBatteryLevelUseCase getBatteryLevelUseCase;
    private final GetLoadedItemUseCase getLoadedItemsUseCase;


    @PostMapping
    public ResponseEntity<?> createBox(@Valid @RequestBody CreateBoxRequest request,
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

    @PostMapping("/{boxId}/load")
    public ResponseEntity<?> loadBox(@PathVariable UUID boxId, @Valid @RequestBody LoadBoxRequest request) {
        Result<BoxView, DomainError> result =
                loadBoxUseCase.execute(new LoadBoxCommand(boxId, request.itemIds()));
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        return ResponseEntity.ok(BoxResponse.from(result.getValue()));
    }

    @GetMapping("/{boxId}/items")
    public ResponseEntity<?> getLoadedItems(@PathVariable UUID boxId) {
        Result<List<ItemView>, DomainError> result = getLoadedItemsUseCase.execute(boxId);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        List<ItemResponse> items = result.getValue().stream().map(ItemResponse::from).toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_READ')")
    public ResponseEntity<List<BoxResponse>> getAvailableBoxes() {
        List<BoxResponse> boxes = getAvailableBoxesUseCase.execute().stream()
                .map(BoxResponse::from)
                .toList();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/{boxId}/battery")
    @PreAuthorize("hasAuthority('PERMISSION_BOX_READ')")
    public ResponseEntity<?> getBatteryLevel(@PathVariable UUID boxId) {
        Result<String, DomainError> result = getBatteryLevelUseCase.execute(boxId);
        if (result.isFailure()) {
            return toErrorResponse(result.getError());
        }

        return ResponseEntity.ok(new BatteryLevelResponse(result.getValue()));
    }

    // Private Method
    private ResponseEntity<ProblemDetail> toErrorResponse(DomainError error) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);

        problemDetail.setTitle("Domain Error");
        problemDetail.setDetail(error.toString());

        return ResponseEntity.badRequest().body(problemDetail);
    }
}


