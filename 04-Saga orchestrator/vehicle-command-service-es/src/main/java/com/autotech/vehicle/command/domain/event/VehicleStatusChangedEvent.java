package com.autotech.vehicle.command.domain.event;

public record VehicleStatusChangedEvent(
        String vin,
        String oldStatus,
        String newStatus
) {}
