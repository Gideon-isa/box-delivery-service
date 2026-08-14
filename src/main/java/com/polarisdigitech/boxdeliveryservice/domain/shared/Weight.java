package com.polarisdigitech.boxdeliveryservice.domain.shared;

public class Weight implements ValueObject {
    private final int grams;

    private Weight(int grams) {
        this.grams = grams;
    }

    public int getGrams() {
        return grams;
    }

    public static Result<Weight, DomainError> of(int grams) {
        if (grams <= 0) {
            return Result.failure(ValidationError.of("weight", "Weight must be greater than zero grams"));
        }
        return Result.success(new Weight(grams));
    }

    public Weight plus(Weight other) {
        return new Weight(this.grams + other.grams);
    }

    public boolean exceeds(Weight limit) {
        return this.grams > limit.grams;
    }
}
