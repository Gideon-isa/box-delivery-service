package com.polarisdigitech.boxdeliveryservice.db.item;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemStatus;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
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
                item.getCreatedAt(),
                item.getCreatedBy());
    }

    public void updateJpaEntity(ItemJpaEntity jpaEntity, Item item) {
        jpaEntity.setStatus(ItemStatusJpa.valueOf(item.getStatus().name()));
        jpaEntity.setBoxId(item.getBoxId() == null ? null : item.getBoxId());
        jpaEntity.setModifiedAt(item.getModifiedAt());
        jpaEntity.setModifiedBy(item.getModifiedBy());
    }

    public Result<Item, DomainError> toDomain(ItemJpaEntity jpaEntity) {
        BoxId boxId = jpaEntity.getBoxId() == null
                ? null
                : BoxId.of(jpaEntity.getBoxId());

        ItemStatus status = ItemStatus.valueOf(jpaEntity.getStatus().name());

        UUID createdBy = jpaEntity.getCreatedBy();
        return Item.reconstitute(
                ItemId.of(jpaEntity.getId()),
                jpaEntity.getName(),
                jpaEntity.getWeightGrams(),
                jpaEntity.getCode(),
                status,
                boxId,
                createdBy
        );
    }
}
