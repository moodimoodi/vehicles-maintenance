package com.autotech.maintenance.saga.domain.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot {

    private int version = 0;
    private final List<Object> uncommittedEvents = new ArrayList<>();

    protected void applyChange(Object event) {
        mutate(event);
        uncommittedEvents.add(event);
        version++;
    }

    protected abstract void mutate(Object event);

    public List<Object> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void markEventsCommitted() {
        uncommittedEvents.clear();
    }

    public int getVersion() {
        return version;
    }

    protected void setVersion(int version) {
        this.version = version;
    }
}
