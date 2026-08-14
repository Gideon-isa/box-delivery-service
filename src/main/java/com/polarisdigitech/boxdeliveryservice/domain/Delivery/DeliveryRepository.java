package com.polarisdigitech.boxdeliveryservice.domain.Delivery;

import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID id);

    List<Delivery> findByBoxId(BoxId boxId);

    List<Delivery> findActive();
}
