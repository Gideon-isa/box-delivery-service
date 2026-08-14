package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.item;

import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.item.Item;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemId;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemStatus;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ItemPersistenceMapper {
    public ItemJpaEntity toNewJpaEntity(Item item) {
        return new ItemJpaEntity(
                item.getId().getValue(),
                item.getName().getName(),
                item.getWeight().getGrams(),
                item.getCode().getCode(),
                ItemStatusJpa.valueOf(item.getStatus().name()),
                item.getBoxId() == null ? null : item.getBoxId(),
                item.getDeleted(),
                item.getCreatedAt(),
                item.getCreatedBy(),
                item.getModifiedAt(),
                item.getModifiedBy()
        );
    }

    public void updateJpaEntity(ItemJpaEntity jpaEntity, Item item) {
        jpaEntity.setStatus(ItemStatusJpa.valueOf(item.getStatus().name()));
        jpaEntity.setBoxId(item.getBoxId() == null ? null : item.getBoxId());
    }

    public Result<Item, DomainError> toDomain(ItemJpaEntity jpaEntity) {
        BoxId boxId = jpaEntity.getBoxId() == null ? null : BoxId.of(jpaEntity.getBoxId());
        UUID createdBy = jpaEntity.getCreatedBy();
        return Item.reconstitute(
                ItemId.of(jpaEntity.getId()),
                jpaEntity.getName(),
                jpaEntity.getWeightGrams(),
                jpaEntity.getCode(),
                ItemStatus.valueOf(jpaEntity.getStatus().name()),
                boxId,
                createdBy
        );
    }
}
