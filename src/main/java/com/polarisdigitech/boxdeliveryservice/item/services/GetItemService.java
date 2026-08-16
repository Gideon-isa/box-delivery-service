package com.polarisdigitech.boxdeliveryservice.item.services;

import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.item.usecases.GetItemUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetItemService implements GetItemUseCase {
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Result<ItemView, DomainError> execute(UUID id) {
        ItemId itemId = ItemId.of(id);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            return Result.failure(new NotFoundError("item", id.toString()));
        }

        Item item = itemOptional.get();
        return Result.success(ItemView.from(item));
    }
}
