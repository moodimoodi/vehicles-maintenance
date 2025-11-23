package com.autotech.maintenance.command.domain.command;

import java.time.LocalDateTime;

public record CompleteMaintenanceCommand(
        String appointmentId,
        LocalDateTime completedAt
) {}
