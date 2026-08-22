package com.polarisdigitech.boxdeliveryservice.item.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.item.usecases.DeleteItemUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.InternalServerError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteItemService implements DeleteItemUseCase {

    private final ItemRepository itemRepository;
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public Result<Boolean, DomainError> execute(UUID id) {
        UUID userId = currentUser.getId();
        ItemId itemId = ItemId.of(id);

        var isDeleted = itemRepository.deleteItemById(itemId, currentUser.getId());
        if (!isDeleted) {
            return Result.failure(InternalServerError.of("500", "something went wrong..."));
        }
        return Result.success(true);
    }
}
