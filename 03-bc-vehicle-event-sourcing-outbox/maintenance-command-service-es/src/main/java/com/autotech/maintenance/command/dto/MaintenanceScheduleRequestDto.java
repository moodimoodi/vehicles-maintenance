package com.autotech.maintenance.command.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceScheduleRequestDto {
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
}
