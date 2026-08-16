package com.polarisdigitech.boxdeliveryservice.delivery.usecases;

import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveredItemCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DeliveredItemResponse;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface DeliveredItemUseCase {
    Result<DeliveredItemResponse, DomainError> execute(DeliveredItemCommand command);
}
