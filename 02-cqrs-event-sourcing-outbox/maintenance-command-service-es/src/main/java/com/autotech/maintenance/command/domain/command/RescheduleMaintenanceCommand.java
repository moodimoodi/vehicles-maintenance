package com.autotech.maintenance.command.domain.command;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
public record RescheduleMaintenanceCommand(
        String appointmentId,
        LocalDateTime newScheduledAt
) {}
