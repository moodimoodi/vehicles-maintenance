package com.autotech.vehicle.command.domain.command;

public record UpdateVehicleDetailsCommand(
        String vin,
        String brand,
        String model,
        Integer year,
        Integer mileage
) {}
