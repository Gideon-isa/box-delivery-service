package com.polarisdigitech.boxdeliveryservice.domain.item;

import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValueObject;

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

    @Override
    public String toString() {
        return value;
    }
}
