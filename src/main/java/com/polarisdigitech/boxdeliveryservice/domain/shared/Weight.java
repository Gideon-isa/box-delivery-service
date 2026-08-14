package com.polarisdigitech.boxdeliveryservice.domain.shared;

public class Weight implements ValueObject {
    private final double grams;

    private Weight(double grams) {
        this.grams = grams;
    }


    public double getGrams() {
        return grams;
    }

    public static Result<Weight, DomainError> of(double grams) {
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
