package com.polarisdigitech.boxdeliveryservice.item.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.dto.CreateItemCommand;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;

public interface CreateItemUseCase {
    Result<ItemView, DomainError> execute(CreateItemCommand command);
}
