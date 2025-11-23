package com.autotech.maintenance.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceViewResponse {

    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
    private String status;
    private LocalDateTime lastUpdatedAt;
}
