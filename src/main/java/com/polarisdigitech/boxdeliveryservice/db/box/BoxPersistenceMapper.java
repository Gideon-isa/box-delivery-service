package com.polarisdigitech.boxdeliveryservice.db.box;

import com.polarisdigitech.boxdeliveryservice.box.domain.*;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import org.springframework.stereotype.Component;

@Component
public class BoxPersistenceMapper {
    public BoxJpaEntity toNewJpaEntity(Box box) {
        return new BoxJpaEntity(
                box.getId().getValue(),
                box.getTxRef().getValue(),
                box.getWeightLimit().getValue(),
                box.getTotalItemWeight().getGrams(),
                box.getBatteryLevel().getPercentage(),
                BoxStateJpa.valueOf(BoxState.IDLE.name()),
                box.getCreatedAt(),
                box.getCreatedBy());
    }

    public void updateJpaEntity(BoxJpaEntity jpaEntity, Box box) {
        jpaEntity.setState(BoxStateJpa.valueOf(box.getState().name()));
        jpaEntity.setTotalItemsWeight(box.getTotalItemWeight().getGrams());
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
