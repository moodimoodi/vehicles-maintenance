package com.autotech.maintenance.saga.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StartMaintenanceBookingRequestDto {
    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
}
