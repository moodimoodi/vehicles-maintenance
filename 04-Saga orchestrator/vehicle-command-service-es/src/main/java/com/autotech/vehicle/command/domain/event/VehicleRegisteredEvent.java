package com.autotech.vehicle.command.domain.event;

public record VehicleRegisteredEvent(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage,
        String status,
        String ownerId
) {}
