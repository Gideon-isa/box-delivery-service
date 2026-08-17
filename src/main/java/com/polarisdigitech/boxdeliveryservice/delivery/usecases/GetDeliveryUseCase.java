package com.polarisdigitech.boxdeliveryservice.delivery.usecases;

import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.UUID;

public interface GetDeliveryUseCase {

    Result<DeliveryView, DomainError> execute(UUID id);

}
