package com.polarisdigitech.boxdeliveryservice.delivery.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxState;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.DeliveryRepository;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.FlightRoundTrip;
import com.polarisdigitech.boxdeliveryservice.delivery.domainservices.DispatchService;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DispatchBoxCommand;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.response.DispatchCreatedResponse;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.DispatchBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemRepository;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemStatus;
import com.polarisdigitech.boxdeliveryservice.shared.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DispatchBoxService implements DispatchBoxUseCase {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;
    private final DeliveryRepository deliveryRepository;
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public Result<DeliveryView, DomainError> execute(DispatchBoxCommand command) {

        UUID userId = currentUser.getId();

        // retrieving box
        Box box = boxRepository.findById(BoxId.of(command.boxId())).orElse(null);
        if (box == null) {
            return Result.failure(NotFoundError.of("Box", command.boxId().toString()));
        }

        // retrieving item's Ids
        List<Item> assignedItems = itemRepository.findByBoxId(BoxId.of(command.boxId()), ItemStatus.ASSIGNED);
        List<ItemId> itemIds = assignedItems.stream().map(Entity::getId).toList();

        var transitionResult = box.transitionTo(BoxState.DELIVERING);
        if (transitionResult.isFailure()) {
            return Result.failure(transitionResult.getError());
        }

        Result<Double, DomainError> tripDistanceResult = Delivery.calculateDistance(
                command.currentLatitude(),
                command.currentLongitude(),
                command.destinationLatitude(),
                command.destinationLongitude());

        if (tripDistanceResult.isFailure()) {
            return Result.failure(tripDistanceResult.getError());
        }

        double deliveryDistance = tripDistanceResult.getValue();

        Result<FlightRoundTrip, DomainError> flightTripResult =  DispatchService.calculateEstimatedDeliveryTime(
                Instant.now(),
                tripDistanceResult.getValue(),
                command.speed(),
                box.getTotalItemWeight().getGrams());

        if (flightTripResult.isFailure()) {
            return Result.failure(flightTripResult.getError());
        }
        Instant estimatedArrivalTime = flightTripResult.getValue().getLocationArrivalTime();
        Result<Delivery, DomainError> deliveryResult = Delivery.create(
                command.remoteLocationName(),
                command.currentLatitude(),
                command.currentLongitude(),
                command.destinationLatitude(),
                command.destinationLongitude(),
                deliveryDistance,
                estimatedArrivalTime,
                command.speed(),
                box.getId(),
                itemIds,
                userId);

        if (deliveryResult.isFailure()) {
            return Result.failure(deliveryResult.getError());
        }
        box.markModified(userId);
        boxRepository.save(box);

        deliveryRepository.save(deliveryResult.getValue());

        return Result.success(DeliveryView.from(deliveryResult.getValue()));
    }
}
