package com.autotech.vehicle.query.messaging.events;

public record VehicleOwnerAssignedEvent(
        String vin,
        String ownerId
) {}
