package com.polarisdigitech.boxdeliveryservice.application.box;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.ItemView;
import com.polarisdigitech.boxdeliveryservice.application.box.port.in.GetLoadedItemUseCase;
import com.polarisdigitech.boxdeliveryservice.domain.box.Box;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
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

