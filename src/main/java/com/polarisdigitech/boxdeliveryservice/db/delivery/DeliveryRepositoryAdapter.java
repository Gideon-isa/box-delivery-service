package com.polarisdigitech.boxdeliveryservice.db.delivery;



import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.DeliveryRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
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
