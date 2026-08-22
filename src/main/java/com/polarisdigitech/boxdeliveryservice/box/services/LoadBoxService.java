package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.LoadBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.LoadBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.LoadBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import com.polarisdigitech.boxdeliveryservice.shared.ValidationError;
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
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public Result<LoadBoxResponse, DomainError> execute(LoadBoxCommand command) {
        if (command.itemIds() == null || command.itemIds().isEmpty()) {
            return Result.failure(ValidationError.of("itemIds", "At least one item id is required"));
        }

        Box box = boxRepository.findById(BoxId.of(command.boxId())).orElse(null);
        if (box == null) {
            return Result.failure(NotFoundError.of("Box", command.boxId().toString()));
        }

        List<ItemId> requestedIds = command
                .itemIds()
                .stream()
                .map(ItemId::of)
                .toList();

        List<Item> items = itemRepository.findAllByIds(requestedIds);

        if (items.size() != requestedIds.size()) {
            return Result.failure(NotFoundError.of(
                    "Item",
                    "one or more of the requested item ids do not exist"));
        }

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
        Box loadedbox = loadResult.getValue();
        loadedbox.markModified(currentUser.getId());

        Box savedBox = boxRepository.save(loadedbox);
        itemRepository.saveAll(assignedItems);

        return Result.success(LoadBoxResponse.from(savedBox));
    }
}
