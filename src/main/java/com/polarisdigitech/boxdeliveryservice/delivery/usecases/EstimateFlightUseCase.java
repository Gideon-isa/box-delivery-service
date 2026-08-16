package com.polarisdigitech.boxdeliveryservice.delivery.usecases;

import com.polarisdigitech.boxdeliveryservice.delivery.dto.EstimateFlightCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.EstimateFlightResponse;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface EstimateFlightUseCase {
    Result<EstimateFlightResponse, DomainError> execute(EstimateFlightCommand command);
}
