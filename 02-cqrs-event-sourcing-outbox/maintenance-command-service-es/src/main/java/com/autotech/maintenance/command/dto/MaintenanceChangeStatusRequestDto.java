package com.autotech.maintenance.command.dto;

import lombok.Data;

@Data
public class MaintenanceChangeStatusRequestDto {
    private String newStatus;
}
