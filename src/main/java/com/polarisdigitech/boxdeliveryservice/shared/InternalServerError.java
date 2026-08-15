package com.polarisdigitech.boxdeliveryservice.shared;

public final class InternalServerError implements DomainError{
    private String field;
    private String message;

    public InternalServerError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public  static  InternalServerError of(String field, String message)  {
        return new InternalServerError(field, message);
    }

    @Override
    public String code() {
        return "INTERNAL_SERVER_ERROR";
    }

    @Override
    public String message() {
        return message;
    }
}
