package com.autotech.vehicle.command.eventstore;

import java.util.List;

public interface VehicleEventStore {

    void save(String aggregateId,
              String aggregateType,
              int expectedVersion,
              List<Object> events);

    List<Object> load(String aggregateId);
}
