package com.autotech.maintenance.command.domain.command;

public record CancelMaintenanceCommand(
        String appointmentId,
        String reason
) {}
