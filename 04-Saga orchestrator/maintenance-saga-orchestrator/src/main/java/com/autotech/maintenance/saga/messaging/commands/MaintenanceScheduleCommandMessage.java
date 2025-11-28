package com.autotech.maintenance.saga.messaging.commands;

public record MaintenanceScheduleCommandMessage(
        String sagaId,
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        String maintenanceType
) {}
