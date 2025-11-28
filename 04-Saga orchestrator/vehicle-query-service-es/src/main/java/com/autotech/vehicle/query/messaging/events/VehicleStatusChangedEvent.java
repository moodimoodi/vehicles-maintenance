package com.autotech.vehicle.query.messaging.events;

public record VehicleStatusChangedEvent(
        String vin,
        String oldStatus,
        String newStatus
) {}
