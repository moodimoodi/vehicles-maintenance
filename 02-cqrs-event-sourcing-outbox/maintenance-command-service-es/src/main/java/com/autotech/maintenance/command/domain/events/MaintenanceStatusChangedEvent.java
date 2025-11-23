package com.autotech.maintenance.command.domain.events;

public record MaintenanceStatusChangedEvent(
        String appointmentId,
        String oldStatus,
        String newStatus
) {}
