package com.autotech.vehicle.command.domain.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Root Aggregate that contains abstract and common logic
 */
public abstract class AggregateRoot {

    protected int version;

    private final List<Object> uncommittedEvents = new ArrayList<>();

    protected void applyChange(Object event) {
        // 1) Appliquer au state (méthode "apply" interne)
        mutate(event);
        // 2) Ajouter aux événements non-committés
        uncommittedEvents.add(event);
    }

    // utilisé pour rejouer l’historique sans marquer comme "uncommitted"
    public void replay(Iterable<Object> history) {
        history.forEach(this::mutate);
        this.uncommittedEvents.clear();
    }

    protected abstract void mutate(Object event);

    public List<Object> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void markEventsCommitted() {
        this.uncommittedEvents.clear();
    }

    public int getVersion() {
        return version;
    }

    protected void incrementVersion() {
        this.version++;
    }
}
