package com.polarisdigitech.boxdeliveryservice.application.box.port.in;

import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;

import java.util.UUID;

public interface GetBatteryLevelUseCase {
    Result<String, DomainError> execute(UUID boxId);
}
