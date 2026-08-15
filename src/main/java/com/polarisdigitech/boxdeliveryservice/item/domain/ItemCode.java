package com.polarisdigitech.boxdeliveryservice.item.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

import java.util.regex.Pattern;

public final class ItemCode implements ValueObject {
    private String value;
    private static final Pattern ALLOWED = Pattern.compile("^[A-Z0-9_]+$");
    private static final int MAX_LENGTH = 50;

    private ItemCode(String value) {
        this.value = value;
    }

    public static Result<ItemCode, DomainError> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure(ValidationError.of("code", "Item code must not be blank"));
        }
        String trimmed = raw.trim();
        if (trimmed.length() > MAX_LENGTH) {
            return Result.failure(ValidationError.of("code", "Item code must not exceed " + MAX_LENGTH + " characters"));
        }
        if (!ALLOWED.matcher(trimmed).matches()) {
            return Result.failure(ValidationError.of("code",
                    "Item code may only contain upper case letters, numbers and underscore ('_')"));
        }
        return Result.success(new ItemCode(trimmed));
    }

    public String getCode() {
        return value;
    }
    @Override
    public String toString() {
        return value;
    }
}
