package com.polarisdigitech.boxdeliveryservice.domain.box;

import com.polarisdigitech.boxdeliveryservice.domain.item.Item;
import com.polarisdigitech.boxdeliveryservice.domain.shared.*;

import java.util.List;
import java.util.UUID;

public final class Box extends AggregateRoot<BoxId> {

    private final TxRef txRef;
    private final WeightLimit weightLimit;
    private BoxState state;
    private Weight totalItemsWeight;
    private Battery batteryLevel;

    private Box(BoxId boxId, UUID createdBy, TxRef txRef, BoxState state, Battery batteryLevel, WeightLimit weightLimit) {
        super(boxId, createdBy);
        this.txRef = txRef;
        this.state = state;
        this.weightLimit = weightLimit;
        this.batteryLevel = batteryLevel;
    }
    public static Result<Box, DomainError> create(String rawTxRef, double weightLimitGrams, double batteryPercentage, UUID createdBy) {
        Result<TxRef, DomainError> txRefResult = TxRef.of(rawTxRef);
        if (txRefResult.isFailure()) {
            return Result.failure(txRefResult.getError());
        }
        Result<WeightLimit, DomainError> weightLimitResult = WeightLimit.of(weightLimitGrams);
        if (weightLimitResult.isFailure()) {
            return Result.failure(weightLimitResult.getError());
        }
        Result<Battery, DomainError> batteryResult = Battery.of(batteryPercentage);
        if (batteryResult.isFailure()) {
            return Result.failure(batteryResult.getError());
        }

        Box box = new Box(
                BoxId.generate(),
                createdBy,
                txRefResult.getValue(),
                BoxState.IDLE,
                batteryResult.getValue(),
                weightLimitResult.getValue());
        return Result.success(box);
    }

    public static Result<Box, DomainError> reconstitute(
            BoxId id, TxRef txRef, WeightLimit weightLimit, Battery batteryLevel,
            BoxState state, double currentWeightGrams, UUID createdBy) {
        if (id == null || txRef == null || weightLimit == null || batteryLevel == null || state == null) {
            return Result.failure(ValidationError.of("box", "Box reconstitution requires all fields to be non-null"));
        }
        Result<Weight, DomainError> weightResult =  Weight.of(currentWeightGrams);
        if (weightResult.isFailure()) {
            return  Result.failure(weightResult.getError());
        }

        return Result.success(new Box(id, createdBy, txRef, state, batteryLevel, weightLimit));
    }

    public Result<Box, DomainError> load(List<Item> itemsToLoad) {
        Result<BoxState, DomainError> transitionResult =
                BoxStateTransition.validate(this.state, BoxState.LOADING, this.batteryLevel);
        if (transitionResult.isFailure()) {
            return Result.failure(transitionResult.getError());
        }

        List<Weight> incomingWeights = itemsToLoad.stream().map(Item::getWeight).toList();
        Result<Weight, DomainError> weightCheck =
                BoxLoadingPolicy.validate(this.weightLimit, this.totalItemsWeight, incomingWeights);
        if (weightCheck.isFailure()) {
            return Result.failure(weightCheck.getError());
        }
        this.state = BoxState.LOADING;
        this.totalItemsWeight = weightCheck.getValue();
        this.state = BoxState.LOADED;

        return Result.success(this);
    }

    public Result<Box, DomainError> transitionTo(BoxState target) {
        Result<BoxState, DomainError> result = BoxStateTransition.validate(this.state, target, this.batteryLevel);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        this.state = target;
        return Result.success(this);
    }

    public boolean isAvailableForLoading() {
        return this.state == BoxState.IDLE && !this.batteryLevel.isBelowLoadingThreshold();
    }


    public TxRef getTxRef() {
        return txRef;
    }

    public WeightLimit getWeightLimit() {
        return weightLimit;
    }

    public Battery getBatteryLevel() {
        return batteryLevel;
    }

    public BoxState getState() {
        return state;
    }

    public Weight getTotalItemWeight() {
        return totalItemsWeight;
    }

    @Override
    public String toString() {
        return "Box{id=%s, txRef=%s, state=%s, battery=%s, weightLimit=%s, currentWeight=%s}"
                .formatted(this.getId(), txRef, state, batteryLevel, weightLimit, totalItemsWeight);
    }

}
