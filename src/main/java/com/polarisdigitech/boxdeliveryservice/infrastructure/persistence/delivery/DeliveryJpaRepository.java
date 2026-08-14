package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryJpaEntity, UUID> {
    @Query(name = "Delivery.findByBoxId")
    List<DeliveryJpaEntity> findByBoxId(@Param("boxId") UUID boxId);

    @Query(name = "Delivery.findActive")
    List<DeliveryJpaEntity> findActive();
}
