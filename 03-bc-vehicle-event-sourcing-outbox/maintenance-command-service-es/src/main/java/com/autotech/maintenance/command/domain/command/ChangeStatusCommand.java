package com.autotech.maintenance.command.domain.command;

public record ChangeStatusCommand(
        String appointmentId,
        String newStatus
) {}