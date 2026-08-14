package com.polarisdigitech.boxdeliveryservice.presentation.box.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record LoadBoxRequest(
        @NotEmpty(message = "At least one item id is required to load a box")
        List<UUID> itemIds
) {
}
