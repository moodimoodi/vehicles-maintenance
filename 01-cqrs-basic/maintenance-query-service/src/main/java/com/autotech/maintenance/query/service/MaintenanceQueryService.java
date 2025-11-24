package com.autotech.maintenance.query.service;

import com.autotech.maintenance.query.dto.CustomerMaintenanceViewResponseDto;
import com.autotech.maintenance.query.dto.WorkshopDailyPlanningViewResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceQueryService {

    List<CustomerMaintenanceViewResponseDto> getCustomerAppointments(String customerId);

    List<WorkshopDailyPlanningViewResponseDto> getWorkshopPlanning(String workshopId, LocalDate date);
}
