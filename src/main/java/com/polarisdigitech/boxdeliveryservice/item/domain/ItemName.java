package com.polarisdigitech.boxdeliveryservice.item.domain;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import com.polarisdigitech.boxdeliveryservice.shared.ValueObject;

import java.util.regex.Pattern;

public final class ItemName implements ValueObject {
    private String value;
    private static final Pattern ALLOWED = Pattern.compile("^[A-Za-z0-9_ -]+$");
    private static final int MAX_LENGTH = 100;
    private ItemName(String value) {
        this.value = value;
    }

    public static Result<ItemName, DomainError> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Result.failure(ValidationError.of("name", "Item name must not be blank"));
        }
        String trimmed = raw.trim();
        if (trimmed.length() > MAX_LENGTH) {
            return Result.failure(ValidationError.of("name", "Item name must not exceed " + MAX_LENGTH + " characters"));
        }
        if (!ALLOWED.matcher(trimmed).matches()) {
            return Result.failure(ValidationError.of("name",
                    "Item name may only contain letters, numbers, hyphen ('-') and underscore ('_')"));
        }
        return Result.success(new ItemName(trimmed));
    }

    public String getName() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
