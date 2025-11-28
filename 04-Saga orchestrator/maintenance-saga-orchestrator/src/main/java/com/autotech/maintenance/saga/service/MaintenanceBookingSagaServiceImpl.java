package com.autotech.maintenance.saga.service;

import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingRequestDto;
import com.autotech.maintenance.saga.controller.dto.StartMaintenanceBookingResponseDto;
import com.autotech.maintenance.saga.controller.dto.VehicleReserveCommandPayloadDto;
import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;
import com.autotech.maintenance.saga.domain.command.StartMaintenanceBookingCommand;
import com.autotech.maintenance.saga.domain.handler.SagaMaintenanceCommandHandler;
import com.autotech.maintenance.saga.domain.saga.repository.MaintenanceBookingSagaRepository;
import com.autotech.maintenance.saga.eventstore.repository.SagaOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceBookingSagaServiceImpl implements MaintenanceBookingSagaService {

    private final MaintenanceBookingSagaRepository sagaRepository;
    private final SagaOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    private final SagaMaintenanceCommandHandler handler;

    @Override
    @Transactional
    public StartMaintenanceBookingResponseDto startSaga(StartMaintenanceBookingRequestDto request) {
        // Saga ID
        String sagaId = UUID.randomUUID().toString();
        String appointmentId = request.getAppointmentId() != null
                ? request.getAppointmentId()
                : "APP-" + sagaId.substring(0, 8);

        var cmd = new StartMaintenanceBookingCommand(
                sagaId,
                appointmentId,
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
    public StartMaintenanceBookingResponseDto reserveVehicle(VehicleReserveCommandPayloadDto request) {
        return null;
    }

    @Override
    public StartMaintenanceBookingResponseDto scheduleMaintenance(StartMaintenanceBookingRequestDto request) {
        return null;
    }

    @Override
    public StartMaintenanceBookingResponseDto cancelMaintnance(StartMaintenanceBookingRequestDto request) {
        return null;
    }

    private StartMaintenanceBookingResponseDto toResponse(MaintenanceBookingSagaAggregate agg) {

        StartMaintenanceBookingResponseDto res = new StartMaintenanceBookingResponseDto();
        res.setSagaId(agg.getSagaId());
        res.setAppointmentId(agg.getAppointmentId());
        res.setStatus(agg.getStatus());
        return res;

    }
}
