package com.autotech.maintenance.query.messaging.events;

import java.time.LocalDateTime;

public record MaintenanceScheduledEvent(
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        LocalDateTime scheduledAt,
        String maintenanceType,
        String status
) {}
