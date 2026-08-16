package com.polarisdigitech.boxdeliveryservice.delivery.usecases;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DispatchBoxCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.EstimateFlightCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DispatchCreatedResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.EstimateFlightResponse;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface DispatchBoxUseCase {
    Result<DeliveryView, DomainError> execute(DispatchBoxCommand command);
}
