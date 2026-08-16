package com.polarisdigitech.boxdeliveryservice.item.dto;

import com.polarisdigitech.boxdeliveryservice.item.domain.Item;

import java.util.UUID;

public record ItemView(UUID id, String name, double weightGrams, String code) {

    public static ItemView from(Item item) {
        return new ItemView(
                item.getId().getValue(),
                item.getName().getName(),
                item.getWeight().getGrams(),
                item.getCode().getCode()
        );

    }
}
