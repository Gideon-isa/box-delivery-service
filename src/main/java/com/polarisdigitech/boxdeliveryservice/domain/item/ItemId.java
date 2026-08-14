package com.polarisdigitech.boxdeliveryservice.domain.item;

import com.polarisdigitech.boxdeliveryservice.domain.shared.ValueObject;

import java.util.UUID;

public final class ItemId implements ValueObject {
    private UUID value;

    private ItemId(UUID value) {
        this.value = value;
    }

    public static ItemId generate() {
        return new ItemId(UUID.randomUUID());
    }

    public static ItemId of(UUID value) {
        return new ItemId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
