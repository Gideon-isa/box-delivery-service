package com.polarisdigitech.boxdeliveryservice.domain.shared;

public sealed interface DomainError permits ValidationError, BusinessRuleViolation, NotFoundError {
    String code();
    String message();
}
