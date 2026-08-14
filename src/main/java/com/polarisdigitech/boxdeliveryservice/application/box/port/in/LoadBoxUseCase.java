package com.polarisdigitech.boxdeliveryservice.application.box.port.in;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;

public interface LoadBoxUseCase {
    Result<BoxView, DomainError> execute(LoadBoxCommand command);
}
