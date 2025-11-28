package com.autotech.vehicle.query.messaging.events;

public record VehicleRegisteredEvent(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage,
        String status,
        String ownerId
) {}
