package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface CreateBoxUseCase {
    Result<BoxView, DomainError> execute(CreateBoxCommand command);
}
