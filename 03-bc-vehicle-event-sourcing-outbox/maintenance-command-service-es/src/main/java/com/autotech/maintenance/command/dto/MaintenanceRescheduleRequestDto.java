package com.autotech.maintenance.command.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceRescheduleRequestDto {
    private LocalDateTime newScheduledAt;
}
