package com.autotech.maintenance.command.domain.command;

import java.time.LocalDateTime;

public record ScheduleMaintenanceCommand(
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        LocalDateTime scheduledAt,
        String maintenanceType
) {}
