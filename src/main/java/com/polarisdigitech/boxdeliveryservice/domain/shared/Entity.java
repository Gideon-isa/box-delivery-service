package com.polarisdigitech.boxdeliveryservice.domain.shared;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public abstract class Entity<ID> {
    private final ID id;
    private boolean isDeleted;

    protected Entity(ID id) {
       this.id = id;
    }

    public ID getId() {
        return id;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Entity<?> that = (Entity<?>) other;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
