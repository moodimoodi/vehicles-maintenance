package com.autotech.maintenance.command.dto;

import lombok.Data;

@Data
public class MaintenanceStatusUpdateRequestDto {

    private String appointmentId;
    private String newStatus;
    private String cancelReason;
}
