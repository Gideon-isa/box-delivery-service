package com.polarisdigitech.boxdeliveryservice.box.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

public class TxRef implements ValueObject {
    private final String value;
    private static final int MAX_LENGTH = 20;

    private TxRef(String value) {
        this.value = value;
    }

    public static Result<TxRef, DomainError> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure(ValidationError.of(TxRef.class.getSimpleName(), "txref must not be blank"));
        }
        String trimmed = raw.trim();
        if (trimmed.length() > MAX_LENGTH) {
            return Result.failure(ValidationError.of(TxRef.class.getSimpleName(),
                    "txref must not exceed " + MAX_LENGTH + " characters"));
        }
        return Result.success(new TxRef(trimmed));
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
