package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.delivery;

import com.polarisdigitech.boxdeliveryservice.domain.Delivery.Delivery;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemId;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryPersistenceMapper {

    public DeliveryJpaEntity toNewJpaEntity(Delivery delivery) {
        return new DeliveryJpaEntity(
                delivery.getId(),
                delivery.getLocationDistance(),
                delivery.getBoxSetSpeed(),
                delivery.getBoxId().getValue(),
                delivery.getItemIds().stream().map(ItemId::getValue).toList(),
                delivery.getStartTime(),
                delivery.getArrivalTime(),
                delivery.getReturnedTime(),
                delivery.isDelivered(),
                delivery.isReturned(),
                delivery.getDeleted(),
                delivery.getCreatedAt(),
                delivery.getCreatedBy(),
                delivery.getModifiedAt(),
                delivery.getModifiedBy()
        );
    }

    public void updateJpaEntity(DeliveryJpaEntity jpaEntity, Delivery delivery) {
        jpaEntity.setArrivalTime(delivery.getArrivalTime());
        jpaEntity.setReturnedTime(delivery.getReturnedTime());
        jpaEntity.setDelivered(delivery.isDelivered());
        jpaEntity.setReturned(delivery.isReturned());
        jpaEntity.setDeleted(delivery.getDeleted());
        jpaEntity.setModifiedAt(delivery.getModifiedAt());
        jpaEntity.setModifiedBy(delivery.getModifiedBy());
        // locationDistance, boxSetSpeed, boxId, itemIds, startTime are immutable
        // post-creation in the domain (final fields) -- nothing to update for them.
    }

    public Result<Delivery, DomainError> toDomain(DeliveryJpaEntity jpaEntity) {
        List<ItemId> itemIds = jpaEntity.getItemIds().stream().map(ItemId::of).toList();

        return Delivery.reconstitute(
                jpaEntity.getId(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getLocationDistance(),
                jpaEntity.getBoxSetSpeed(),
                BoxId.of(jpaEntity.getBoxId()),
                itemIds,
                jpaEntity.getStartTime(),
                jpaEntity.getArrivalTime(),
                jpaEntity.getReturnedTime(),
                jpaEntity.isDelivered(),
                jpaEntity.isReturned()
        );
    }
}
