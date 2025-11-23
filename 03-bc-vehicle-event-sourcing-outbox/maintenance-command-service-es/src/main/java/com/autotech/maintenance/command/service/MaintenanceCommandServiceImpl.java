package com.autotech.maintenance.command.service;

import com.autotech.maintenance.command.domain.aggregate.MaintenanceAppointmentAgrregate;
import com.autotech.maintenance.command.domain.command.*;
import com.autotech.maintenance.command.domain.handler.MaintenanceCommandHandler;
import com.autotech.maintenance.command.dto.MaintenanceChangeStatusRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceRescheduleRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceResponseDto;
import com.autotech.maintenance.command.dto.MaintenanceScheduleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service to manage maintenance life cycle.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceCommandServiceImpl implements MaintenanceCommandService {

    private final MaintenanceCommandHandler handler;

    @Override
    public MaintenanceResponseDto schedule(MaintenanceScheduleRequestDto request) {
        String id = UUID.randomUUID().toString();
        var cmd = new ScheduleMaintenanceCommand(
                id,
                request.getVehicleVin(),
                request.getCustomerId(),
                request.getWorkshopId(),
                request.getScheduledAt(),
                request.getMaintenanceType()
        );
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public MaintenanceResponseDto reschedule(String appointmentId, MaintenanceRescheduleRequestDto request) {
        var cmd = new RescheduleMaintenanceCommand(appointmentId, request.getNewScheduledAt());
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public MaintenanceResponseDto changeStatus(String appointmentId, MaintenanceChangeStatusRequestDto request) {

        var cmd = new ChangeStatusCommand(appointmentId, request.getNewStatus());
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public MaintenanceResponseDto cancel(String appointmentId, String reason) {
        var cmd = new CancelMaintenanceCommand(appointmentId, reason);
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    @Override
    public MaintenanceResponseDto complete(String appointmentId) {
        var cmd = new CompleteMaintenanceCommand(appointmentId,  null);
        var agg = handler.handle(cmd);
        return toResponse(agg);
    }

    private MaintenanceResponseDto toResponse(MaintenanceAppointmentAgrregate agg) {
        MaintenanceResponseDto res = new MaintenanceResponseDto();
        res.setAppointmentId(agg.getAppointmentId());
        res.setVehicleVin(agg.getVehicleVin());
        res.setCustomerId(agg.getCustomerId());
        res.setWorkshopId(agg.getWorkshopId());
        res.setScheduledAt(agg.getScheduledAt());
        res.setMaintenanceType(agg.getMaintenanceType());
        res.setStatus(agg.getStatus());
        res.setVersion(agg.getVersion());
        return res;
    }
}
