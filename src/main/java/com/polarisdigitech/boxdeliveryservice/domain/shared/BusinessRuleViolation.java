package com.polarisdigitech.boxdeliveryservice.domain.shared;

public final class BusinessRuleViolation implements DomainError {
    private String rule;
    private String message;

    public BusinessRuleViolation(String rule, String message) {
        this.rule = rule;
        this.message = message;
    }

    public static BusinessRuleViolation of(String rule, String message) {
        return new BusinessRuleViolation(rule, message);
    }

    @Override
    public String code() {
        return "BUSINESS_RULE_VIOLATION";
    }

    @Override
    public String message() {
        return "";
    }

}
