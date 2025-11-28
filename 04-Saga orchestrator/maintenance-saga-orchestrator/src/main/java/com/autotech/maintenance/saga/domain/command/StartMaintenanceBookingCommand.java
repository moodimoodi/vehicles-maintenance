package com.autotech.maintenance.saga.domain.command;

import java.time.LocalDateTime;

public record StartMaintenanceBookingCommand(
        String sagaId,
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        LocalDateTime scheduledAt,
        String maintenanceType
) implements SagaCommand {
}
