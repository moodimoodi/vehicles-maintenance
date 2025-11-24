package com.autotech.maintenance.query.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkshopDailyPlanningViewResponseDto {

    private String appointmentId;
    private String workshopId;
    private String workshopName;
    private LocalDate date;
    private String slot;
    private String vin;
    private String vehicleModel;
    private String status;
    private String bay;
}
