package com.polarisdigitech.boxdeliveryservice.item.dto.response;

import com.polarisdigitech.boxdeliveryservice.box.dto.ItemView;

import java.util.List;
import java.util.UUID;

public record ItemResponse(UUID id, String name, double weightGrams, String code) {

    public static ItemResponse from(ItemView view) {
        return new ItemResponse(view.id(), view.name(), view.weightGrams(), view.code());
    }
}
