package com.autotech.maintenance.command.domain.events;

public record MaintenanceCancelledEvent(
        String appointmentId,
        String reason
) {}
