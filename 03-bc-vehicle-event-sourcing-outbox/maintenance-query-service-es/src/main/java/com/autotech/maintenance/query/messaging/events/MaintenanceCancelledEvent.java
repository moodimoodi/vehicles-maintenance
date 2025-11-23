package com.autotech.maintenance.query.messaging.events;

public record MaintenanceCancelledEvent(
        String appointmentId,
        String reason
) {}
