package com.autotech.maintenance.command.service;

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentRequestDto;
import com.autotech.maintenance.command.dto.MaintenanceAppointmentResponseDto;
import com.autotech.maintenance.command.dto.MaintenanceStatusUpdateRequestDto;
import com.autotech.maintenance.command.entity.MaintenanceAppointmentEntity;
import com.autotech.maintenance.command.exception.MaintenanceBusinessException;
import com.autotech.maintenance.command.exception.MaintenanceNotFoundException;
import com.autotech.maintenance.command.mapper.MaintenanceMapper;
import com.autotech.maintenance.command.messaging.DomainEventPublisher;
import com.autotech.maintenance.command.repository.MaintenanceAppointmentRepository;
import com.autotech.maintenance.core.events.maintenance.MaintenanceCompletedEvent;
import com.autotech.maintenance.core.events.maintenance.MaintenanceScheduledEvent;
import com.autotech.maintenance.core.events.maintenance.MaintenanceStatusChangedEvent;
import com.autotech.maintenance.core.messaging.DomainEventEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class MaintenanceCommandServiceImpl implements MaintenanceCommandService {

    private final MaintenanceAppointmentRepository repository;
    private final MaintenanceMapper mapper;
    private final DomainEventPublisher eventPublisher;

    private static final String TOPIC_SCHEDULED = "maintenance-scheduled";
    private static final String TOPIC_STATUS_CHANGED = "maintenance-status-changed";
    private static final String TOPIC_COMPLETED = "maintenance-completed";

    public MaintenanceCommandServiceImpl(MaintenanceAppointmentRepository repository,
                                         MaintenanceMapper mapper,
                                         DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public MaintenanceAppointmentResponseDto scheduleAppointment(MaintenanceAppointmentRequestDto request) {

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new MaintenanceBusinessException(
                    MaintenanceConstants.ERROR_BUSINESS_RULE_VIOLATION,
                    "Appointment date cannot be in the past"
            );
        }

        MaintenanceAppointmentEntity entity = mapper.toEntity(request);
        entity.setAppointmentId(UUID.randomUUID().toString());
        entity.setStatus(MaintenanceConstants.STATUS_CREATED);

        repository.save(entity);

        MaintenanceScheduledEvent payload = MaintenanceScheduledEvent.builder()
                .appointmentId(entity.getAppointmentId())
                .vin(entity.getVin())
                .customerId(entity.getCustomerId())
                .workshopId(entity.getWorkshopId())
                .date(entity.getDate())
                .slot(entity.getSlot())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .requestedServices(entity.getRequestedServices())
                .build();

        DomainEventEnvelope<MaintenanceScheduledEvent> envelope =
                DomainEventEnvelope.<MaintenanceScheduledEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(entity.getAppointmentId())
                        .eventType("MaintenanceScheduled")
                        .aggregateType("MaintenanceAppointment")
                        .occurredAt(Instant.now())
                        .correlationId(entity.getCustomerId())
                        .payload(payload)
                        .build();

        eventPublisher.publish(TOPIC_SCHEDULED, envelope);
        log.info("Maintenance appointment {} scheduled and event published", entity.getAppointmentId());

        return mapper.toResponse(entity);
    }

    @Override
    public void updateStatus(MaintenanceStatusUpdateRequestDto request) {
        MaintenanceAppointmentEntity entity = repository.findById(request.getAppointmentId())
                .orElseThrow(() -> new MaintenanceNotFoundException(request.getAppointmentId()));

        entity.setStatus(request.getNewStatus());
        repository.save(entity);

        MaintenanceStatusChangedEvent payload = MaintenanceStatusChangedEvent.builder()
                .appointmentId(entity.getAppointmentId())
                .newStatus(entity.getStatus())
                .cancelReason(request.getCancelReason())
                .build();

        DomainEventEnvelope<MaintenanceStatusChangedEvent> envelope =
                DomainEventEnvelope.<MaintenanceStatusChangedEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(entity.getAppointmentId())
                        .eventType("MaintenanceStatusChanged")
                        .aggregateType("MaintenanceAppointment")
                        .occurredAt(Instant.now())
                        .correlationId(entity.getCustomerId())
                        .payload(payload)
                        .build();

        eventPublisher.publish(TOPIC_STATUS_CHANGED, envelope);
        log.info("Maintenance appointment {} status updated and event published", entity.getAppointmentId());
    }

    @Override
    public void completeAppointment(String appointmentId, int finalMileage) {
        MaintenanceAppointmentEntity entity = repository.findById(appointmentId)
                .orElseThrow(() -> new MaintenanceNotFoundException(appointmentId));

        if (MaintenanceConstants.STATUS_CANCELLED.equals(entity.getStatus())) {
            throw new MaintenanceBusinessException(
                    MaintenanceConstants.ERROR_BUSINESS_RULE_VIOLATION,
                    "Cancelled appointment cannot be completed"
            );
        }

        entity.setStatus(MaintenanceConstants.STATUS_COMPLETED);
        repository.save(entity);

        MaintenanceCompletedEvent payload = MaintenanceCompletedEvent.builder()
                .appointmentId(entity.getAppointmentId())
                .finalMileage(finalMileage)
                .build();

        DomainEventEnvelope<MaintenanceCompletedEvent> envelope =
                DomainEventEnvelope.<MaintenanceCompletedEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(entity.getAppointmentId())
                        .eventType("MaintenanceCompleted")
                        .aggregateType("MaintenanceAppointment")
                        .occurredAt(Instant.now())
                        .correlationId(entity.getCustomerId())
                        .payload(payload)
                        .build();

        eventPublisher.publish(TOPIC_COMPLETED, envelope);
        log.info("Maintenance appointment {} completed and event published", entity.getAppointmentId());
    }
    @Override
    public List<MaintenanceAppointmentResponseDto> findByWorkshopIdAndDate(String workshopId, LocalDate date) {

        List<MaintenanceAppointmentEntity> entities = new ArrayList<MaintenanceAppointmentEntity>();

        if (workshopId != null && date != null) {
            entities = repository.findByWorkshopIdAndDate(workshopId, date);
        }

        return mapper.toResponse(entities);
    }
}
