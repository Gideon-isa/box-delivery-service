package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.response.ReturnBoxResponse;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.UUID;

public interface ReturnedBoxUseCase {
    Result<ReturnBoxResponse, DomainError> execute(UUID boxId);
}
