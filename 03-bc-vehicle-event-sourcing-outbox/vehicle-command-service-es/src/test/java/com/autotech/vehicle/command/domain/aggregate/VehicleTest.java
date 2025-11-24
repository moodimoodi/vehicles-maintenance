package com.autotech.vehicle.command.domain.aggregate;

import com.autotech.vehicle.command.domain.event.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void registerNew_shouldCreateVehicleWithActiveStatusByDefault() {
        VehicleAggregate vehicle = VehicleAggregate.registerNew(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                null,
                "CUST-1"
        );

        assertEquals("VIN-123", vehicle.getVin());
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals(2020, vehicle.getYear());
        assertEquals(30000, vehicle.getMileage());
        assertEquals("ACTIVE", vehicle.getStatus());
        assertEquals("CUST-1", vehicle.getOwnerId());

        List<Object> events = vehicle.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof VehicleRegisteredEvent);
    }

    @Test
    void updateDetails_shouldProduceEventAndUpdateState() {
        VehicleAggregate vehicle = VehicleAggregate.registerNew(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                null
        );
        vehicle.markEventsCommitted();

        vehicle.updateDetails("Toyota", "Corolla Hybrid", 2021, 35000);

        assertEquals("Corolla Hybrid", vehicle.getModel());
        assertEquals(2021, vehicle.getYear());
        assertEquals(35000, vehicle.getMileage());

        List<Object> events = vehicle.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof VehicleDetailsUpdatedEvent);
    }

    @Test
    void changeStatus_shouldProduceEventWhenStatusChanges() {
        VehicleAggregate vehicle = VehicleAggregate.registerNew(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                null
        );
        vehicle.markEventsCommitted();

        vehicle.changeStatus("INACTIVE");

        assertEquals("INACTIVE", vehicle.getStatus());

        List<Object> events = vehicle.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof VehicleStatusChangedEvent);
    }

    @Test
    void changeStatus_shouldNotProduceEventWhenStatusIsSame() {
        VehicleAggregate vehicle = VehicleAggregate.registerNew(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                null
        );
        vehicle.markEventsCommitted();

        vehicle.changeStatus("ACTIVE");

        assertTrue(vehicle.getUncommittedEvents().isEmpty());
    }

    @Test
    void assignAndUnassignOwner_shouldProduceEventsAndUpdateState() {
        VehicleAggregate vehicle = VehicleAggregate.registerNew(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                null
        );
        vehicle.markEventsCommitted();

        vehicle.assignOwner("CUST-1");
        assertEquals("CUST-1", vehicle.getOwnerId());
        assertTrue(vehicle.getUncommittedEvents().get(0) instanceof VehicleOwnerAssignedEvent);

        vehicle.markEventsCommitted();
        vehicle.unassignOwner();
        assertNull(vehicle.getOwnerId());
        assertTrue(vehicle.getUncommittedEvents().get(0) instanceof VehicleOwnerUnassignedEvent);
    }
}
