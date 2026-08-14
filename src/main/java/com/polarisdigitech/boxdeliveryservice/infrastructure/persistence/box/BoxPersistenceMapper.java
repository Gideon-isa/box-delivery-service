package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.box;

import com.polarisdigitech.boxdeliveryservice.domain.box.*;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Weight;
import org.springframework.stereotype.Component;

@Component
public class BoxPersistenceMapper {
    public BoxJpaEntity toNewJpaEntity(Box box) {
        return new BoxJpaEntity(
                box.getId().getValue(),
                box.getTxRef().getValue(),
                box.getWeightLimit().getValue(),
                box.getBatteryLevel().getPercentage(),
                BoxStateJpa.valueOf(box.getState().name()),
                box.gettotalItemWeight().getGrams(),
                box.getDeleted(),
                box.getCreatedAt(), box.getCreatedBy(), box.getModifiedAt(), box.getModifiedBy()
        );
    }

    public void updateJpaEntity(BoxJpaEntity jpaEntity, Box box) {
        jpaEntity.setState(BoxStateJpa.valueOf(box.getState().name()));
        jpaEntity.setTotalItemsWeight(box.gettotalItemWeight().getGrams());
    }

    public Result<Box, DomainError> toDomain(BoxJpaEntity jpaEntity) {
        return TxRef.of(jpaEntity.getTxRef())
                .flatMap(txRef -> WeightLimit.of(jpaEntity.getWeightLimit())
                        .flatMap(weightLimit -> Battery.of(jpaEntity.getBatteryLevel())
                                .flatMap(battery -> Box.reconstitute(
                                        BoxId.of(jpaEntity.getId()),
                                        txRef,
                                        weightLimit,
                                        battery,
                                        BoxState.valueOf(jpaEntity.getState().name()),
                                        jpaEntity.getTotalItemsWeight(),
                                        jpaEntity.getCreatedBy()))));
    }
}
