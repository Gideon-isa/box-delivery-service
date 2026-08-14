package com.polarisdigitech.boxdeliveryservice.application.box.port.in;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;

public interface CreateBoxUseCase {
    Result<BoxView, DomainError> execute(CreateBoxCommand command);
}
