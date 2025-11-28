package com.autotech.maintenance.command.domain.events;

import java.time.LocalDateTime;

public record MaintenanceRescheduledEvent(
        String appointmentId,
        LocalDateTime oldScheduledAt,
        LocalDateTime newScheduledAt
) {}
