package com.autotech.maintenance.saga.domain.saga.events;

import java.time.LocalDateTime;

public record SagaStartedEvent(
        String sagaId,
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        LocalDateTime scheduledAt,
        String maintenanceType
) {}
