package com.polarisdigitech.boxdeliveryservice.domain.box;

import com.polarisdigitech.boxdeliveryservice.domain.shared.ValueObject;

import java.util.UUID;

public class BoxId implements ValueObject {
    private UUID value;

    private BoxId(UUID value) {
    }

    public UUID getValue() {
        return value;
    }
    public static BoxId generate() {
        return new BoxId(UUID.randomUUID());
    }

    public static BoxId of(UUID value) {
        return new BoxId(value);
    }
}
