package com.polarisdigitech.boxdeliveryservice.item.usecases;

import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.List;

public interface GetAvailableItemsUseCase {
    Result<List<ItemView>, DomainError> execute();
}
