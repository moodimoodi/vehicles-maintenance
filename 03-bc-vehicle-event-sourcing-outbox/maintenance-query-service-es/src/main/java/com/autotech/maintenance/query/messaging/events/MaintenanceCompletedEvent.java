package com.autotech.maintenance.query.messaging.events;

import java.time.LocalDateTime;

public record MaintenanceCompletedEvent(
        String appointmentId,
        LocalDateTime completedAt
) {}
