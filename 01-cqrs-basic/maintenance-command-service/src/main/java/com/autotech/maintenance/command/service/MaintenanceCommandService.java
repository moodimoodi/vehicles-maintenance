package com.autotech.maintenance.command.service;

import com.autotech.maintenance.command.dto.MaintenanceAppointmentRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentResponseDto;
import com.autotech.maintenance.command.dto.MaintenanceStatusUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceCommandService {

    MaintenanceAppointmentResponseDto scheduleAppointment(MaintenanceAppointmentRequestDto request);

    void updateStatus(MaintenanceStatusUpdateRequestDto request);

    void completeAppointment(String appointmentId, int finalMileage);

    List<MaintenanceAppointmentResponseDto> findByWorkshopIdAndDate(String workshopId, LocalDate date);
}
