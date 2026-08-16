package com.polarisdigitech.boxdeliveryservice.db.box;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.TxRef;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoxRepositoryAdapter implements BoxRepository {
    private final BoxJpaRepository jpaRepository;
    private final BoxPersistenceMapper mapper;


    @Override
    public Box save(Box box) {
        UUID boxId = box.getId().getValue();
        BoxJpaEntity jpaEntity = jpaRepository
                .findById(boxId)
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
                .filter(Result::isSuccess)
                .map(Result::getValue);
    }

    @Override
    public Optional<Box> findByTxRef(TxRef txRef) {
        return jpaRepository.findByTxRef(txRef.getValue())
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue);
    }

    @Override
    public List<Box> findAvailableForLoading() {

        return jpaRepository.findAvailableForLoading()
                .stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    @Query(name = "Box.existsByTxRef")
    @Override
    public boolean existsByTxRef(TxRef txRef) {
        return jpaRepository.existsByTxRef(txRef.getValue());
    }
}
