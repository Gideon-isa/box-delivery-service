package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID id);

    List<Delivery> findByBoxId(BoxId boxId);

    List<Delivery> findActive();
}
