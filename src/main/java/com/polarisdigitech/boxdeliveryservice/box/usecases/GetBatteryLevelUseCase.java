package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.UUID;

public interface GetBatteryLevelUseCase {
    Result<String, DomainError> execute(UUID boxId);
}
