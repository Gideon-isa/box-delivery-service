package com.polarisdigitech.boxdeliveryservice.item.dto.request;

import jakarta.validation.constraints.*;

public record CreateItemRequest(

         @Pattern(regexp = "^[a-zA-Z0-9_ -]+$", message = "only letters, number, -, and _ are allowed")
        String name,

        @NotNull(message = "weightLimitGrams is required")
        @Min(value = 1, message = "weightLimitGrams must be greater than zero")
        @Max(value = 500, message = "weightLimitGrams must not exceed 500")
        Double weight,

        @NotNull(message = "code is required")
        @Pattern(regexp = "^[A-Z0-9_ -]+$", message = "only upper case letter")
        String code
){

}
