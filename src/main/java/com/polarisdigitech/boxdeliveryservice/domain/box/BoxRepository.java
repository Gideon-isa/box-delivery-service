package com.polarisdigitech.boxdeliveryservice.domain.box;

import java.util.List;
import java.util.Optional;

public interface BoxRepository {
    Box save(Box box);

    Optional<Box> findById(BoxId id);

    Optional<Box> findByTxRef(TxRef txRef);

    /** Boxes currently in IDLE state with battery >= the loading threshold. */
    List<Box> findAvailableForLoading();

    boolean existsByTxRef(TxRef txRef);
}
