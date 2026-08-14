package com.polarisdigitech.boxdeliveryservice.application.box;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.application.box.port.in.LoadBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.domain.box.Box;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.domain.item.Item;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemId;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadBoxService implements LoadBoxUseCase {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Result<BoxView, DomainError> execute(LoadBoxCommand command) {
        if (command.itemIds() == null || command.itemIds().isEmpty()) {
            return Result.failure(ValidationError.of("itemIds", "At least one item id is required"));
        }

        Box box = boxRepository.findById(BoxId.of(command.boxId())).orElse(null);
        if (box == null) {
            return Result.failure(NotFoundError.of("Box", command.boxId().toString()));
        }

        List<ItemId> requestedIds = command.itemIds().stream().map(ItemId::of).toList();
        List<Item> items = itemRepository.findAllByIds(requestedIds);

        if (items.size() != requestedIds.size()) {
            return Result.failure(NotFoundError.of("Item", "one or more of the requested item ids do not exist"));
        }

        // Assign each item first — this is where "already assigned elsewhere" is caught,
        // per Item's own invariant. Only once every item can legally be assigned do we
        // touch Box's weight/state invariant, so a failure here leaves Box untouched.
        List<Item> assignedItems = new ArrayList<>();
        for (Item item : items) {
            Result<Item, DomainError> assignResult = item.assignToBox(box.getId());
            if (assignResult.isFailure()) {
                return Result.failure(assignResult.getError());
            }
            assignedItems.add(assignResult.getValue());
        }

        Result<Box, DomainError> loadResult = box.load(assignedItems);
        if (loadResult.isFailure()) {
            return Result.failure(loadResult.getError());
        }

        Box savedBox = boxRepository.save(loadResult.getValue());
        itemRepository.saveAll(assignedItems);

        return Result.success(BoxView.from(savedBox));
    }
}
