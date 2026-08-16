package com.polarisdigitech.boxdeliveryservice.box.domain;

import com.polarisdigitech.boxdeliveryservice.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class BoxStateTransition {
    private static final Map<BoxState, Set<BoxState>> LEGAL_TRANSITIONS = new EnumMap<>(BoxState.class);

    static {
        LEGAL_TRANSITIONS.put(BoxState.IDLE, EnumSet.of(BoxState.LOADING));
        LEGAL_TRANSITIONS.put(BoxState.LOADING, EnumSet.of(BoxState.LOADED, BoxState.IDLE));
        LEGAL_TRANSITIONS.put(BoxState.LOADED, EnumSet.of(BoxState.DELIVERING, BoxState.IDLE));
        LEGAL_TRANSITIONS.put(BoxState.DELIVERING, EnumSet.of(BoxState.DELIVERED, BoxState.RETURNING));
        LEGAL_TRANSITIONS.put(BoxState.DELIVERED, EnumSet.of(BoxState.RETURNING));
        LEGAL_TRANSITIONS.put(BoxState.RETURNING, EnumSet.of(BoxState.IDLE));
    }

    public BoxStateTransition() {
    }

    public static Result<BoxState, DomainError> validate(BoxState current, BoxState target, Battery battery) {
        Set<BoxState> allowedTargets = LEGAL_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowedTargets.contains(target)) {
            return Result.failure(BusinessRuleViolation.of(
                    "ILLEGAL_STATE_TRANSITION",
                    "Cannot transition box from " + current + " to " + target));
        }

        if (target == BoxState.LOADING && battery.isBelowLoadingThreshold()) {
            return Result.failure(BusinessRuleViolation.of(
                    "BATTERY_TOO_LOW_FOR_LOADING",
                    "Box battery is " + battery + ", must be at least "
                            + Battery.MIN_LOADING_THRESHOLD + "% to enter LOADING state"));
        }

        return Result.success(target);
    }

}
