package com.polarisdigitech.boxdeliveryservice.item.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.domain.*;
import com.polarisdigitech.boxdeliveryservice.item.dto.CreateItemCommand;
import com.polarisdigitech.boxdeliveryservice.item.usecases.CreateItemUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateItemService implements CreateItemUseCase {

    private final ItemRepository itemRepository;
    private final CurrentUser currentUser;


    @Override
    @Transactional
    public Result<ItemView, DomainError> execute(CreateItemCommand command) {
        UUID userId = currentUser.getId();

         Result<ItemCode, DomainError> codeResult =  ItemCode.of(command.code());
         if (codeResult.isFailure()) {
             return Result.failure(codeResult.getError());
         }
        var isExist = itemRepository.existsByCode(codeResult.getValue());

        if (isExist) {
            return Result.failure(ValidationError.of("code", "code already exists"));
        }

        Result<Item, DomainError> itemResult = Item.create(command.name(), userId, command.weight(), command.code());
        if (itemResult.isFailure()) {
            return Result.failure(itemResult.getError());
        }
        Item item = itemRepository.save(itemResult.getValue());
        return Result.success(ItemView.from(item));
    }
}
