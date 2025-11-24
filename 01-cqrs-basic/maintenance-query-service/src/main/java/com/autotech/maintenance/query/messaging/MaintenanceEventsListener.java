package com.autotech.maintenance.query.messaging;

import com.autotech.maintenance.query.entity.CustomerMaintenanceViewEntity;
import com.autotech.maintenance.query.entity.WorkshopDailyPlanningViewEntity;
import com.autotech.maintenance.query.messaging.events.MaintenanceCompletedEvent;
import com.autotech.maintenance.query.messaging.events.MaintenanceScheduledEvent;
import com.autotech.maintenance.query.messaging.events.MaintenanceStatusChangedEvent;
import com.autotech.maintenance.query.repository.CustomerMaintenanceViewRepository;
import com.autotech.maintenance.query.repository.WorkshopDailyPlanningViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MaintenanceEventsListener {

    private final CustomerMaintenanceViewRepository customerRepo;
    private final WorkshopDailyPlanningViewRepository workshopRepo;
    private final ObjectMapper objectMapper;

    public MaintenanceEventsListener(CustomerMaintenanceViewRepository customerRepo,
                                     WorkshopDailyPlanningViewRepository workshopRepo,
                                     ObjectMapper objectMapper) {
        this.customerRepo = customerRepo;
        this.workshopRepo = workshopRepo;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "maintenance-scheduled",
            groupId = "maintenance-query-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleMaintenanceScheduled(DomainEventEnvelope<?> envelope) {
        log.info("Received MaintenanceScheduled envelope: {}", envelope);

        MaintenanceScheduledEvent event =
                objectMapper.convertValue(envelope.getPayload(), MaintenanceScheduledEvent.class);

        CustomerMaintenanceViewEntity customerView = new CustomerMaintenanceViewEntity();
        customerView.setAppointmentId(event.getAppointmentId());
        customerView.setCustomerId(event.getCustomerId());
        customerView.setVin(event.getVin());
        customerView.setVehicleModel(null);
        customerView.setWorkshopId(event.getWorkshopId());
        customerView.setWorkshopName(null);
        customerView.setDate(event.getDate());
        customerView.setSlot(event.getSlot());
        customerView.setStatus(event.getStatus());
        customerView.setReason(event.getReason());
        customerRepo.save(customerView);

        WorkshopDailyPlanningViewEntity workshopView = new WorkshopDailyPlanningViewEntity();
        workshopView.setAppointmentId(event.getAppointmentId());
        workshopView.setWorkshopId(event.getWorkshopId());
        workshopView.setWorkshopName(null);
        workshopView.setDate(event.getDate());
        workshopView.setSlot(event.getSlot());
        workshopView.setVin(event.getVin());
        workshopView.setVehicleModel(null);
        workshopView.setStatus(event.getStatus());
        workshopView.setBay(null);
        workshopRepo.save(workshopView);
    }

    @KafkaListener(
            topics = "maintenance-status-changed",
            groupId = "maintenance-query-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleMaintenanceStatusChanged(DomainEventEnvelope<?> envelope) {
        log.info("Received MaintenanceStatusChanged envelope: {}", envelope);

        MaintenanceStatusChangedEvent event =
                objectMapper.convertValue(envelope.getPayload(), MaintenanceStatusChangedEvent.class);

        customerRepo.findById(event.getAppointmentId()).ifPresent(view -> {
            view.setStatus(event.getNewStatus());
            if ("CANCELLED".equalsIgnoreCase(event.getNewStatus())
                    && event.getCancelReason() != null) {
                view.setReason(event.getCancelReason());
            }
            customerRepo.save(view);
        });

        workshopRepo.findById(event.getAppointmentId()).ifPresent(view -> {
            view.setStatus(event.getNewStatus());
            workshopRepo.save(view);
        });
    }

    @KafkaListener(
            topics = "maintenance-completed",
            groupId = "maintenance-query-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleMaintenanceCompleted(DomainEventEnvelope<?> envelope) {
        log.info("Received MaintenanceCompleted envelope: {}", envelope);

        MaintenanceCompletedEvent event =
                objectMapper.convertValue(envelope.getPayload(), MaintenanceCompletedEvent.class);

        customerRepo.findById(event.getAppointmentId()).ifPresent(view -> {
            view.setStatus("COMPLETED");
            customerRepo.save(view);
        });

        workshopRepo.findById(event.getAppointmentId()).ifPresent(view -> {
            view.setStatus("COMPLETED");
            workshopRepo.save(view);
        });
    }
}
