package com.autotech.vehicle.command.domain.command;

public record AssignOwnerCommand(
        String vin,
        String ownerId
) {}
