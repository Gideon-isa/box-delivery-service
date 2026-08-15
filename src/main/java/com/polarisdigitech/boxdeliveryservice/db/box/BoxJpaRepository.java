package com.polarisdigitech.boxdeliveryservice.db.box;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoxJpaRepository extends JpaRepository<BoxJpaEntity, UUID> {

    @Query(name = "Box.findByTxRef")
    Optional<BoxJpaEntity> findByTxRef(@Param("txRef") String txRef);

    @Query(name = "Box.existsByTxRef")
    boolean existsByTxRef(@Param("txRef") String txRef);

    @Query(name = "Box.findAvailableForLoading")
    List<BoxJpaEntity> findAvailableForLoading();
}
