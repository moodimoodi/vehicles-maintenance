package com.autotech.maintenance.query.messaging.events;

public record MaintenanceStatusChangedEvent(
        String appointmentId,
        String oldStatus,
        String newStatus
) {}
