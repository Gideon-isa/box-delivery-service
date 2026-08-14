package com.polarisdigitech.boxdeliveryservice.domain.shared;

public record NotFoundError(String resource, String identifier) implements  DomainError {

    public static NotFoundError of(String resource, String identifier) {
        return new NotFoundError(resource, identifier);
    }
    @Override
    public String code() {
        return "NOT_FOUND";
    }

    @Override
    public String message() {
        return resource + " not found: " + identifier;
    }
}
