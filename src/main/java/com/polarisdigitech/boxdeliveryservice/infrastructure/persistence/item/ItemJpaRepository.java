package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.item;

import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {

    @Query(name = "ItemEntity.findByBoxId")
    List<ItemJpaEntity> findByBoxId(@Param("boxId") UUID boxId);

    @Query(name = "ItemEntity.findAllByIdIn")
    List<ItemJpaEntity> findAllByIdIn(@Param("ids") List<UUID> ids);
}
