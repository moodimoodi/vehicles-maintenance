package com.autotech.maintenance.saga.controller.dto;

public record MaintenanceScheduleCommandPayloadDto(
        String sagaId,
        String appointmentId,
        String vehicleVin,
        String customerId,
        String workshopId,
        String maintenanceType
) {}
