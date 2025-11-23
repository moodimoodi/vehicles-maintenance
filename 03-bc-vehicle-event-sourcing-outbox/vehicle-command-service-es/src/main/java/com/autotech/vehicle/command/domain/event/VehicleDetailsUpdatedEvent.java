package com.autotech.vehicle.command.domain.event;

public record VehicleDetailsUpdatedEvent(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage
) {}
