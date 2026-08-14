package com.polarisdigitech.boxdeliveryservice.domain.box;

import com.polarisdigitech.boxdeliveryservice.domain.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Weight;

import java.util.List;

/**
 * Domain service enforcing the weight-capacity rule when loading items into a box:
 * a box must never carry more weight than its limit allows.
 */
public final class BoxLoadingPolicy {

    private BoxLoadingPolicy() {}

    public static Result<Weight, DomainError> validate(
            WeightLimit weightLimit, Weight currentWeight, List<Weight> incomingWeights) {

        int incomingGrams = incomingWeights
                .stream()
                .mapToInt(Weight::getGrams)
                .sum();

        Result<Weight, DomainError> weightResult =  Weight.of(incomingGrams);
        if (weightResult.isFailure()) {
            return Result.failure(weightResult.getError());
        }
        Weight projectedTotal = currentWeight.plus(weightResult.getValue());
        if (weightLimit.isExceeded(projectedTotal)) {
            return Result.failure(BusinessRuleViolation.of(
                    "WEIGHT_LIMIT_EXCEEDED",
                    "Loading these items would bring the box to " + projectedTotal
                            + ", which exceeds its weight limit of " + weightLimit));
        }

        return Result.success(projectedTotal);
    }
}
