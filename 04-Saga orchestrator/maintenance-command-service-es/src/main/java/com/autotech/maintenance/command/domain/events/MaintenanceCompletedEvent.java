package com.autotech.maintenance.command.domain.events;

import java.time.LocalDateTime;

public record MaintenanceCompletedEvent(
        String appointmentId,
        LocalDateTime completedAt
) {}
