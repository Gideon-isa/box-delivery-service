package com.polarisdigitech.boxdeliveryservice.domain.item;

import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    List<Item> saveAll(List<Item> items);

    Optional<Item> findById(ItemId id);

    /** Fetches multiple items by id in one round trip — used when loading a box with several items. */
    List<Item> findAllByIds(List<ItemId> ids);

    List<Item> findByBoxId(BoxId boxId);
}
