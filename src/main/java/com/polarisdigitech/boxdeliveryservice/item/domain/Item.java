package com.polarisdigitech.boxdeliveryservice.item.domain;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.shared.*;

import java.util.UUID;

public final class Item extends AggregateRoot<ItemId> {

    private final ItemName name;
    private final Weight weight;
    private final ItemCode code;
    private ItemStatus status;
    private BoxId boxId;

    private Item(ItemId id, UUID createdBy, ItemName name, Weight weight, ItemCode code, ItemStatus status, BoxId boxId) {
        super(id, createdBy);
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.status = status;
        this.boxId = boxId;
    }

    public static Result<Item, DomainError> create(String rawName, UUID createdBy, double weightGrams, String rawCode) {
        return build(ItemId.generate(), rawName, createdBy, weightGrams, rawCode, ItemStatus.UNASSIGNED, null);
    }

    public static Result<Item, DomainError> reconstitute(
            ItemId id, String rawName, double weightGrams, String rawCode, ItemStatus status, BoxId boxId, UUID createdBy) {
        return build(id, rawName, createdBy, weightGrams, rawCode, status, boxId);
    }

    private static Result<Item, DomainError> build(
            ItemId id,
            String rawName,
            UUID createdBy,
            double weightGrams,
            String rawCode,
            ItemStatus status,
            BoxId boxId) {
        if (id == null) {
            return Result.failure(ValidationError.of("id", "Item id must not be null"));
        }

        Result<ItemName, DomainError> nameResult = ItemName.of(rawName);
        if (nameResult.isFailure()) {
            return Result.failure(nameResult.getError());
        }

        Result<Weight, DomainError> weightResult = Weight.of(weightGrams);
        if (weightResult.isFailure()) {
            return Result.failure(weightResult.getError());
        }

        Result<ItemCode, DomainError> codeResult = ItemCode.of(rawCode);
        if (codeResult.isFailure()) {
            return Result.failure(codeResult.getError());
        }

        return Result.success(
                new Item(id,
                        createdBy,
                        nameResult.getValue(),
                        weightResult.getValue(),
                        codeResult.getValue(),
                        status,
                        boxId));
    }

    public Result<Item, DomainError> assignToBox(BoxId targetBoxId) {
        if (this.status == ItemStatus.ASSIGNED) {
            return Result.failure(BusinessRuleViolation.of(
                    "ITEM_ALREADY_ASSIGNED",
                    "Item " + this.getId() + " is already assigned to box " + this.boxId));
        }
        this.status = ItemStatus.ASSIGNED;
        this.boxId = targetBoxId;
        return Result.success(this);
    }

    /** Releases this item back to the unassigned pool (e.g. if a box load is reversed). */
    public Item unassign() {
        this.status = ItemStatus.UNASSIGNED;
        this.boxId = null;
        return this;
    }

    public boolean isAssigned() {
        return status == ItemStatus.ASSIGNED;
    }

    public ItemName getName() {
        return name;
    }

    public Weight getWeight() {
        return weight;
    }

    public ItemCode getCode() {
        return code;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public UUID getBoxId() {
        return boxId.getValue();
    }

    @Override
    public String toString() {
        return "Item{id=%s, name=%s, weight=%s, code=%s, status=%s, boxId=%s}"
                .formatted(this.getId(), name, weight, code, status, boxId);
    }

}
