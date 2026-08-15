package com.polarisdigitech.boxdeliveryservice.item.usecases;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.UUID;

public interface DeleteItemUseCase {
    Result<Boolean, DomainError> execute(UUID id);
}
