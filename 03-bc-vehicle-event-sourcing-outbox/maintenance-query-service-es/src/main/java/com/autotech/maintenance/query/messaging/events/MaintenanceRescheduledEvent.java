package com.autotech.maintenance.query.messaging.events;

import java.time.LocalDateTime;

public record MaintenanceRescheduledEvent(
        String appointmentId,
        LocalDateTime oldScheduledAt,
        LocalDateTime newScheduledAt
) {}
