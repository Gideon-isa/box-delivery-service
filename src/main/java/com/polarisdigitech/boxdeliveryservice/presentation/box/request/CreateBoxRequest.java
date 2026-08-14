package com.polarisdigitech.boxdeliveryservice.presentation.box.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoxRequest(
        @NotBlank(message = "txRef is required")
        @jakarta.validation.constraints.Size(max = 20, message = "txRef must not exceed 20 characters")
        String txRef,

        @NotNull(message = "weightLimitGrams is required")
        @Min(value = 1, message = "weightLimitGrams must be greater than zero")
        @Max(value = 500, message = "weightLimitGrams must not exceed 500")
        Integer weightLimitGrams,

        @NotNull(message = "batteryPercentage is required")
        @Min(value = 0, message = "batteryPercentage must be between 0 and 100")
        @Max(value = 100, message = "batteryPercentage must be between 0 and 100")
        Integer batteryPercentage
) {
}
