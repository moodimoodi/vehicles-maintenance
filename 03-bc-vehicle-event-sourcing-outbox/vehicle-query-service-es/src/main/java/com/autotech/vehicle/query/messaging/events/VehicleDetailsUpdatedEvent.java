package com.autotech.vehicle.query.messaging.events;

public record VehicleDetailsUpdatedEvent(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage
) {}
