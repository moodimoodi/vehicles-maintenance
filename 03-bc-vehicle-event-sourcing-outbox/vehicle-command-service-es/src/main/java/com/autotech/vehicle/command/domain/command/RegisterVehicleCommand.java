package com.autotech.vehicle.command.domain.command;

public record RegisterVehicleCommand(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage,
        String status,
        String ownerId
) {}
