package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.box;

import com.polarisdigitech.boxdeliveryservice.domain.box.Box;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.domain.box.TxRef;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoxRepositoryAdapter implements BoxRepository {
    private final BoxJpaRepository jpaRepository;
    private final BoxPersistenceMapper mapper;


    @Override
    public Box save(Box box) {
        BoxJpaEntity jpaEntity = jpaRepository.findById(box.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(existing, box);
                    return existing;
                })
                .orElseGet(() -> mapper.toNewJpaEntity(box));

        BoxJpaEntity saved = jpaRepository.save(jpaEntity);

        return mapper.toDomain(saved).getValue();
    }

    @Override
    public Optional<Box> findById(BoxId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain)
                .filter(result -> result.isSuccess())
                .map(result -> result.getValue());
    }

    @Override
    public Optional<Box> findByTxRef(TxRef txRef) {
        return jpaRepository.findByTxRef(txRef.getValue())
                .map(mapper::toDomain)
                .filter(result -> result.isSuccess())
                .map(result -> result.getValue());
    }

    @Override
    public List<Box> findAvailableForLoading() {
        return jpaRepository.findAvailableForLoading().stream()
                .map(mapper::toDomain)
                .filter(result -> result.isSuccess())
                .map(result -> result.getValue())
                .toList();
    }
    @Query(name = "Box.existsByTxRef")
    @Override
    public boolean existsByTxRef(TxRef txRef) {
        return jpaRepository.existsByTxRef(txRef.getValue());
    }
}
