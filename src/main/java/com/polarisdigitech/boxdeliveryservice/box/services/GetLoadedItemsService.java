package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.box.usecases.GetLoadedItemUseCase;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetLoadedItemsService implements GetLoadedItemUseCase {
    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public Result<List<ItemView>, DomainError> execute(UUID boxId) {

        BoxId id = BoxId.of(boxId);

        if (boxRepository.findById(id).isEmpty()) {
            return Result.failure(NotFoundError.of("Box", boxId.toString()));
        }

        List<ItemView> items = itemRepository.findByBoxId(id).stream()
                .map(ItemView::from)
                .toList();
        return Result.success(items);
    }
}

