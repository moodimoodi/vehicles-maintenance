package com.autotech.vehicle.command.domain.event;

public record VehicleOwnerAssignedEvent(
        String vin,
        String ownerId
) {}
