package com.autotech.maintenance.command.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MaintenanceAppointmentRequestDto {

    private String vin;
    private String customerId;
    private String workshopId;
    private LocalDate date;
    private String slot;
    private String reason;
    private List<String> requestedServices;
}
