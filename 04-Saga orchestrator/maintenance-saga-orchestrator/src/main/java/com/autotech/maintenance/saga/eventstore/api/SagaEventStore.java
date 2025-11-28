package com.autotech.maintenance.saga.eventstore.api;

import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;

import java.util.List;

public interface SagaEventStore {
    void save(MaintenanceBookingSagaAggregate saga, String sagaType, int expectedVersion, List<Object> events);
    List<Object> load(String sagaId);
}
