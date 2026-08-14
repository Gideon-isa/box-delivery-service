package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.delivery;



import com.polarisdigitech.boxdeliveryservice.domain.Delivery.Delivery;
import com.polarisdigitech.boxdeliveryservice.domain.Delivery.DeliveryRepository;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepository {

    private final DeliveryJpaRepository jpaRepository;
    private final DeliveryPersistenceMapper mapper;

    @Override
    public Delivery save(Delivery delivery) {
        DeliveryJpaEntity jpaEntity = jpaRepository.findById(delivery.getId())
                .map(existing -> {
                    mapper.updateJpaEntity(existing, delivery);
                    return existing;
                })
                .orElseGet(() -> mapper.toNewJpaEntity(delivery));

        return mapper.toDomain(jpaRepository.save(jpaEntity)).getValue();
    }

    @Override
    public Optional<Delivery> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue);
    }

    @Override
    public List<Delivery> findByBoxId(BoxId boxId) {
        return jpaRepository.findByBoxId(boxId.getValue()).stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    @Override
    public List<Delivery> findActive() {
        return jpaRepository.findActive().stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

}
