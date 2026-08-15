package com.polarisdigitech.boxdeliveryservice.shared;

public sealed interface DomainError permits ValidationError, BusinessRuleViolation, NotFoundError, InternalServerError {
    String code();
    String message();
}
