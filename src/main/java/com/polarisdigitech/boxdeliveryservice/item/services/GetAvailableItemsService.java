package com.polarisdigitech.boxdeliveryservice.item.services;

import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemStatus;
import com.polarisdigitech.boxdeliveryservice.item.usecases.GetAvailableItemsUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAvailableItemsService implements GetAvailableItemsUseCase {

    private final ItemRepository itemRepository;

    @Override
    public Result<List<ItemView>, DomainError> execute() {
        List<Item> items = itemRepository.findAllAvailable(ItemStatus.UNASSIGNED);
        var list = items.stream()
                .map(ItemView::from)
                .toList();

        return  Result.success(list);
    }
}
