package com.polarisdigitech.boxdeliveryservice.db.item;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.*;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemJpaRepository itemJpaRepository;
    private final ItemPersistenceMapper mapper;

    @Override
    public Item save(Item item) {
        ItemJpaEntity jpaEntity = itemJpaRepository.findById(item.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(existing, item);
                    return existing;
                })
                .orElseGet(() -> mapper.toNewJpaEntity(item));

        return mapper.toDomain(itemJpaRepository.save(jpaEntity)).getValue();
    }

    @Override
    public List<Item> saveAll(List<Item> items)  {

        List<ItemId> ids = items.stream()
                .map(Item::getId)
                .toList();

        List<ItemJpaEntity> entities =
                itemJpaRepository.findAllById(
                        ids.stream()
                                .map(ItemId::getValue)
                                .toList()
                );

        Map<UUID, ItemJpaEntity> entityMap = entities.stream()
                .collect(Collectors.toMap(
                        ItemJpaEntity::getId,
                        Function.identity()
                ));

        for (Item item : items) {
            ItemJpaEntity entity = entityMap.get(item.getId().getValue());

            mapper.updateJpaEntity(entity, item);
        }

        return entities.stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    @Override
    public Optional<Item> findById(ItemId id) {
        return itemJpaRepository.findById(id.getValue())
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue);
    }

    @Override
    public List<Item> findAllByIds(List<ItemId> ids) {
        List<UUID> uuidList = ids
                .stream()
                .map(ItemId::getValue)
                .toList();

        return itemJpaRepository
                .findAllByIds(uuidList)
                .stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    @Override
    public List<Item> findByBoxId(BoxId boxId) {
        return itemJpaRepository
                .findByBoxId(boxId.getValue())
                .stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();
    }

    @Override
    public boolean existsByCode(ItemCode code) {
        return itemJpaRepository.existsByCode(code.getCode());
    }

    @Override
    public boolean deleteItemById(ItemId id) {
        return itemJpaRepository.deleteItemById(id.getValue());
    }

    @Override
    public List<Item> findAllAvailable(ItemStatus status) {
        return itemJpaRepository.findAllAvailable(ItemStatusJpa.valueOf(status.name()))
                .stream()
                .map(mapper::toDomain)
                .filter(Result::isSuccess)
                .map(Result::getValue)
                .toList();

    }

}
