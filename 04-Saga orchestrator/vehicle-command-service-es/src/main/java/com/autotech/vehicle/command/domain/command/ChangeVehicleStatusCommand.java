package com.autotech.vehicle.command.domain.command;

public record ChangeVehicleStatusCommand(
        String vin,
        String newStatus
) {}
