package com.polarisdigitech.boxdeliveryservice.db.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {

    @Query(name = "ItemEntity.findByBoxId")
    List<ItemJpaEntity> findByBoxId(@Param("boxId") UUID boxId, @Param("status") ItemStatusJpa status);

    @Query(name = "ItemEntity.findAllByIds")
    List<ItemJpaEntity> findAllByIds(@Param("ids") List<UUID> ids);

    @Query(name = "ItemEntity.findById")
    Optional<ItemJpaEntity> findById(@Param("id") UUID id);

    @Query(name = "ItemEntity.existByCode")
    boolean existsByCode(@Param("code") String code);

    @Query(name = "ItemEntity.deleteById")
    boolean deleteItemById(@Param("id") UUID id);

    @Query(name = "ItemEntity.findAllAvailable")
    List<ItemJpaEntity> findAllAvailable(@Param("status") ItemStatusJpa status);

}
