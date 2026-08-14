package com.polarisdigitech.boxdeliveryservice.domain.shared;

public final class ValidationError implements DomainError {
    private String field;
    private String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public  static  ValidationError of(String field, String message)  {
        return new ValidationError(field, message);
    }

    @Override
    public String code() {
        return "VALIDATION_ERROR";
    }

    @Override
    public String message() {
        return "";
    }
}
