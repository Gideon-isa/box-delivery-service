package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.box;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoxJpaRepository extends JpaRepository<BoxJpaEntity, UUID> {
    Optional<BoxJpaEntity> findByTxRef(@Param("txRef") String txRef);

    boolean existsByTxRef(@Param("txRef") String txRef);

    List<BoxJpaEntity> findAvailableForLoading();
}
