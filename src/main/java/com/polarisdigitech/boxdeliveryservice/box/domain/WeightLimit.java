package com.polarisdigitech.boxdeliveryservice.box.domain;

import com.polarisdigitech.boxdeliveryservice.shared.*;

public class WeightLimit implements ValueObject {
    public static final short MAX_GRAMS = 500;
    private final Weight value;

    private WeightLimit(Weight value) {
        this.value = value;
    }

    public static Result<WeightLimit, DomainError> of(double grams) {
        if (grams <= 0) {
            return Result.failure(ValidationError.of(WeightLimit.class.getSimpleName(),
                    "Weight limit must be greater than zero grams"));
        }
        if (grams > MAX_GRAMS) {
            return Result.failure(ValidationError.of(WeightLimit.class.getSimpleName(),
                    "Weight limit must must not exceed " + MAX_GRAMS + " grams"));
        }
        Result<Weight, DomainError> weightResult = Weight.of(grams);
        if (weightResult.isFailure()) {
            return Result.failure(ValidationError.of(WeightLimit.class.getSimpleName(), weightResult.getError().message()));
        }
        return Result.success(new WeightLimit(weightResult.getValue()));
    }
    public double getValue() {
        return value.getGrams();
    }
    public boolean isExceeded(Weight totalWeight) {
        return totalWeight.exceeds(totalWeight);
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
