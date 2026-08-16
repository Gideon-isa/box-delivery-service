package com.polarisdigitech.boxdeliveryservice.delivery.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxState;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.DeliveryRepository;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveredItemCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DeliveredItemResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DeliveredItemUseCase;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemStatus;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveredItemService implements DeliveredItemUseCase {

    private final DeliveryRepository deliveryRepository;
    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;
    private final CurrentUser currentUser;

    @Transactional
    @Override
    public Result<DeliveredItemResponse, DomainError> execute(DeliveredItemCommand command) {

        // TODO plugin the keycloak
        //UUID userId = currentUser.getId();
        UUID userId = UUID.randomUUID();

        Optional<Delivery> OptionalDelivery = deliveryRepository.findById(command.deliveryId());
        if (OptionalDelivery.isEmpty()) {
            return Result.failure(NotFoundError.of("delivery not found", command.deliveryId().toString()));
        }

        Delivery delivery = OptionalDelivery.get();
        BoxId boxId =  delivery.getBoxId();

        Optional<Box> boxOptional = boxRepository.findById(boxId);
        if (boxOptional.isEmpty()) {
            return Result.failure(NotFoundError.of("delivery box record can not be found", boxId.toString()));
        }

        Box box = boxOptional.get();
        Result<Box, DomainError> updatedBoxResult = box.transitionTo(BoxState.DELIVERED);
        if (updatedBoxResult.isFailure()) {
            return Result.failure(updatedBoxResult.getError());
        }

        List<Item> loadedItems = itemRepository.findByBoxId(boxId, ItemStatus.ASSIGNED);

        List<Item> deliveredItems = itemRepository.findByBoxId(boxId, ItemStatus.ASSIGNED)
                .stream()
                .map(Item::unassign)
                .toList();

        // update delivery record
        Result<Delivery, DomainError> deliveredResult = delivery.markAsDelivered();
        if (deliveredResult.isFailure()) {
            return Result.failure(deliveredResult.getError());
        }

        box.markModified(userId);
        delivery.markModified(userId);
        deliveredItems.forEach(i -> i.markModified(userId));

        itemRepository.saveAll(deliveredItems);
        boxRepository.save(box);
        deliveryRepository.save(delivery);

        return Result.success(DeliveredItemResponse.from(deliveredItems));
    }
}
