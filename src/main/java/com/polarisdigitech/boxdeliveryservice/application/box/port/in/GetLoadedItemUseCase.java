package com.polarisdigitech.boxdeliveryservice.application.box.port.in;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;

import java.util.List;
import java.util.UUID;

public interface GetLoadedItemUseCase {
    Result<List<ItemView>, DomainError> execute(UUID boxId);
}
