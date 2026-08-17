package com.polarisdigitech.boxdeliveryservice.domain.item;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemTest {

    @Test
    void acceptsValidNameAndCode() {
        Result<Item, DomainError> result = Item.create("Widget_1-A",  java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 60,"WIDGET_001");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().isAssigned()).isFalse();
    }

    @Test
    void rejectsNameWithDisallowedCharacters() {
        Result<Item, DomainError> result = Item.create("Widget!", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 70,"WIDGET_001");

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void rejectsCodeWithLowercaseLetters() {
        Result<Item, DomainError> result = Item.create("Widget", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 90, "widget_001");

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void rejectsCodeWithDisallowedSymbols() {
        Result<Item, DomainError> result = Item.create("Widget", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 90, "WIDGET-001*");

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void rejectsZeroOrNegativeWeight() {
        assertThat(Item.create("Widget", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), -90, "WIDGET_001").isFailure()).isTrue();
        assertThat(Item.create("Widget", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 0, "WIDGET_001").isFailure()).isTrue();
    }


    @Test
    void unassignReturnsItemToUnassignedPool() {
        Item item = Item.create("Widget", java.util.UUID.fromString("7f3a9c21-4b08-4e15-9a72-1d5c8f04b6e3"), 90, "WIDGET_001").getValue();
        item.assignToBox(BoxId.generate());

        item.unassign();

        assertThat(item.isAssigned()).isFalse();
        assertThat(item.getBoxId()).isNull();
    }
}
