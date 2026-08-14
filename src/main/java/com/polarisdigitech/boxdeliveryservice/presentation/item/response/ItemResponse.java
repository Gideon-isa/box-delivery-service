package com.polarisdigitech.boxdeliveryservice.presentation.item.response;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.ItemView;

import java.util.UUID;

public record ItemResponse(UUID id, String name, double weightGrams, String code) {
    public static ItemResponse from(ItemView view) {
        return new ItemResponse(view.id(), view.name(), view.weightGrams(), view.code());
    }
}
