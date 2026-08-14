package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.item;

import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.item.Item;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemId;
import com.polarisdigitech.boxdeliveryservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemJpaRepository jpaRepository;
    private final ItemPersistenceMapper mapper;

    @Override
    public Item save(Item item) {
        ItemJpaEntity jpaEntity = jpaRepository.findById(item.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(existing, item);
                    return existing;
                })
                .orElseGet(() -> mapper.toNewJpaEntity(item));

        return mapper.toDomain(jpaRepository.save(jpaEntity)).getValue();
    }

    @Override
    public List<Item> saveAll(List<Item> items) {
        return List.of();
    }

    @Override
    public Optional<Item> findById(ItemId id) {
        return Optional.empty();
    }

    @Override
    public List<Item> findAllByIds(List<ItemId> ids) {
        return List.of();
    }

    @Override
    public List<Item> findByBoxId(BoxId boxId) {
        return List.of();
    }
}
