package com.polarisdigitech.boxdeliveryservice.item.domain;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.TxRef;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    List<Item> saveAll(List<Item> items) ;

    Optional<Item> findById(ItemId id);

    List<Item> findAllByIds(List<ItemId> ids);

    List<Item> findByBoxId(BoxId boxId);

    boolean existsByCode(ItemCode code);

    boolean deleteItemById(ItemId id);

    List<Item> findAllAvailable(ItemStatus status);
}
