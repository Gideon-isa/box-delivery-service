package com.polarisdigitech.boxdeliveryservice.db.delivery;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryPersistenceMapper {

    public DeliveryJpaEntity toNewJpaEntity(Delivery delivery) {
        return new DeliveryJpaEntity(
                delivery.getId(),
                delivery.getDestinationName(),
                delivery.getDispatchedLocationLatitude(),
                delivery.getDispatchedLocationLongitude(),
                delivery.getDestinationLatitude(),
                delivery.getDestinationLongitude(),
                delivery.getDestinationDistance(),
                delivery.getBoxSetSpeed(),
                delivery.getBoxId().getValue(),
                delivery.getItemIds().stream().map(ItemId::getValue).toList(),
                delivery.getStartTime(),
                delivery.getEstimatedArrivalTime(),
                delivery.getArrivalTime(),
                delivery.isDelivered(),
                delivery.isReturned(),
                delivery.getDeleted(),
                delivery.getCreatedAt(),
                delivery.getCreatedBy(),
                delivery.getModifiedAt(),
                delivery.getModifiedBy(),
                delivery.getId().version()
        );
    }

    public void updateJpaEntity(DeliveryJpaEntity jpaEntity, Delivery delivery) {
        jpaEntity.setArrivalTime(delivery.getArrivalTime());
        jpaEntity.setDelivered(delivery.isDelivered());
        jpaEntity.setReturned(delivery.isReturned());
        jpaEntity.setDeleted(delivery.getDeleted());
        jpaEntity.setModifiedAt(delivery.getModifiedAt());
        jpaEntity.setModifiedBy(delivery.getModifiedBy());

    }

    public Result<Delivery, DomainError> toDomain(DeliveryJpaEntity jpaEntity) {
        List<ItemId> itemIds = jpaEntity.getItemIds().stream().map(ItemId::of).toList();

        return Delivery.reconstitute(
                jpaEntity.getId(),
                jpaEntity.getDestinationName(),
                jpaEntity.getDispatchedLocationLatitude(),
                jpaEntity.getDispatchedLocationLongitude(),
                jpaEntity.getDestinationLatitude(),
                jpaEntity.getDestinationLongitude(),
                jpaEntity.getDestinationDistance(),
                jpaEntity.getBoxSetSpeed(),
                BoxId.of(jpaEntity.getBoxId()),
                itemIds,
                jpaEntity.getStartTime(),
                jpaEntity.getEstimatedArrivalTime(),
                jpaEntity.getArrivalTime(),
                jpaEntity.isDelivered(),
                jpaEntity.isReturned(),
                jpaEntity.getCreatedBy()
        );
    }
}
