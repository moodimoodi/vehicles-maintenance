package com.autotech.maintenance.command.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceResponseDto {
    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
    private String status;
    private int version;
}
