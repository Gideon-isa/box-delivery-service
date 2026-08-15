package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.LoadBoxResponse;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface LoadBoxUseCase {
    Result<LoadBoxResponse, DomainError> execute(LoadBoxCommand command);
}
