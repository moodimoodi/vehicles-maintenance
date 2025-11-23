package com.autotech.vehicle.command.domain.repository;

import com.autotech.vehicle.command.domain.aggregate.Vehicle;
import com.autotech.vehicle.command.eventstore.VehicleEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VehicleAggregateRepository {

    private final VehicleEventStore eventStore;

    public Vehicle load(String vin) {
        List<Object> history = eventStore.load(vin);
        if (history.isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found: " + vin);
        }
        Vehicle agg = new Vehicle();
        agg.replay(history);
        return agg;
    }

    public void save(Vehicle agg) {
        var events = agg.getUncommittedEvents();
        if (events.isEmpty()) return;
        int expectedVersion = agg.getVersion() - events.size();
        eventStore.save(agg.getVin(), "Vehicle", expectedVersion, events);
        agg.markEventsCommitted();
    }
}
