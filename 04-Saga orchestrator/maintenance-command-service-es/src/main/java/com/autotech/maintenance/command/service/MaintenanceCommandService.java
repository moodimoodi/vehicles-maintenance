package com.autotech.maintenance.command.service;

import com.autotech.maintenance.command.dto.*;

public interface MaintenanceCommandService {

    MaintenanceResponseDto schedule(MaintenanceScheduleRequestDto request);

    MaintenanceResponseDto reschedule(String appointmentId, MaintenanceRescheduleRequestDto request);

    MaintenanceResponseDto changeStatus(String appointmentId, MaintenanceChangeStatusRequestDto request);

    MaintenanceResponseDto cancel(String appointmentId, String reason);

    MaintenanceResponseDto complete(String appointmentId);
}
