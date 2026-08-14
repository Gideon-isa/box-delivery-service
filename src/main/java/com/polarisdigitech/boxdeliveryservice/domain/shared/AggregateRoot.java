package com.polarisdigitech.boxdeliveryservice.domain.shared;

import java.util.*;

public abstract class AggregateRoot<ID> extends AuditableEntity<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id, UUID createdBy) {
        super(id, createdBy);
    }
    public abstract ID getId();

    protected void raise(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }
}
