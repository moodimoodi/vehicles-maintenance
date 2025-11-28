package com.autotech.maintenance.command.eventstore.api;

import java.util.List;

public interface MaintenanceEventStore {

    void save(String aggregateId,
              String aggregateType,
              int expectedVersion,
              List<Object> events);

    List<Object> load(String aggregateId);
}
