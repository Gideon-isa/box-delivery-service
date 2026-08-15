package com.polarisdigitech.boxdeliveryservice.item.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.dto.CreateItemCommand;
import com.polarisdigitech.boxdeliveryservice.item.dto.GetItemCommand;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

import java.util.UUID;

public interface GetItemUseCase {
    Result<ItemView, DomainError> execute(UUID id);
}
