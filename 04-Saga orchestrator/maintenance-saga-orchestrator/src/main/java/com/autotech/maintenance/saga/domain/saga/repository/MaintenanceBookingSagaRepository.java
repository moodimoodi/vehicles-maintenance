package com.autotech.maintenance.saga.domain.saga.repository;

import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;
import com.autotech.maintenance.saga.eventstore.api.SagaEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MaintenanceBookingSagaRepository {

    private static final String SAGA_TYPE = "MaintenanceBookingSaga";
    private final SagaEventStore eventStore;

    public MaintenanceBookingSagaAggregate load(String sagaId) {
        List<Object> history = eventStore.load(sagaId);
        if (history.isEmpty()) {
            throw new IllegalArgumentException("Saga not found: " + sagaId);
        }
        MaintenanceBookingSagaAggregate saga = new MaintenanceBookingSagaAggregate();
        saga.replay(history);
        return saga;
    }

    public void save(MaintenanceBookingSagaAggregate saga) {
        var events = saga.getUncommittedEvents();
        if (events.isEmpty()) return;
        int expectedVersion = saga.getVersion() - events.size();
        eventStore.save(saga, SAGA_TYPE, expectedVersion, events);
        saga.markEventsCommitted();
    }
}
