package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.List;
import java.util.UUID;

public interface GetLoadedItemUseCase {
    Result<List<ItemView>, DomainError> execute(UUID boxId);
}
