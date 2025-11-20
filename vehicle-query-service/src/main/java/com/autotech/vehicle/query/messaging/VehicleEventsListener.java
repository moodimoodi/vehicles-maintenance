package com.autotech.vehicle.query.messaging;

import com.autotech.vehicle.query.entity.CustomerGarageViewEntity;
import com.autotech.vehicle.query.entity.VehicleOverviewViewEntity;
import com.autotech.vehicle.query.messaging.events.VehicleOwnershipChangedEvent;
import com.autotech.vehicle.query.messaging.events.VehicleRegisteredEvent;
import com.autotech.vehicle.query.messaging.events.VehicleUpdatedEvent;
import com.autotech.vehicle.query.repository.CustomerGarageViewRepository;
import com.autotech.vehicle.query.repository.VehicleOverviewViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VehicleEventsListener {

    private final VehicleOverviewViewRepository overviewRepo;
    private final CustomerGarageViewRepository garageRepo;
    private final ObjectMapper objectMapper;

    public VehicleEventsListener(VehicleOverviewViewRepository overviewRepo,
                                 CustomerGarageViewRepository garageRepo,
                                 ObjectMapper objectMapper) {
        this.overviewRepo = overviewRepo;
        this.garageRepo = garageRepo;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "vehicle-registered", groupId = "vehicle-query-service")
    public void handleVehicleRegistered(DomainEventEnvelope<?> envelope) {
        log.info("Received VehicleRegistered envelope: {}", envelope);
        VehicleRegisteredEvent event = objectMapper.convertValue(envelope.getPayload(), VehicleRegisteredEvent.class);

        VehicleOverviewViewEntity overview = new VehicleOverviewViewEntity();
        overview.setVin(event.getVin());
        overview.setBrand(event.getBrand());
        overview.setModel(event.getModel());
        overview.setYearOfProduction(event.getYearOfProduction());
        overview.setColor(event.getColor());
        overview.setCurrentMileage(event.getCurrentMileage());
        overview.setCustomerId(event.getCustomerId());
        overview.setRegistrationDate(event.getRegistrationDate());
        overviewRepo.save(overview);

        CustomerGarageViewEntity garage = new CustomerGarageViewEntity();
        garage.setId(event.getCustomerId() + ":" + event.getVin());
        garage.setCustomerId(event.getCustomerId());
        garage.setVin(event.getVin());
        garage.setBrand(event.getBrand());
        garage.setModel(event.getModel());
        garageRepo.save(garage);
    }

    @KafkaListener(topics = "vehicle-updated", groupId = "vehicle-query-service")
    public void handleVehicleUpdated(DomainEventEnvelope<?> envelope) {
        log.info("Received VehicleUpdated envelope: {}", envelope);
        VehicleUpdatedEvent event = objectMapper.convertValue(envelope.getPayload(), VehicleUpdatedEvent.class);

        overviewRepo.findById(event.getVin()).ifPresent(overview -> {
            overview.setColor(event.getColor());
            overview.setCurrentMileage(event.getCurrentMileage());
            overviewRepo.save(overview);
        });
    }

    @KafkaListener(topics = "vehicle-ownership-changed", groupId = "vehicle-query-service")
    public void handleVehicleOwnershipChanged(DomainEventEnvelope<?> envelope) {
        log.info("Received VehicleOwnershipChanged envelope: {}", envelope);
        VehicleOwnershipChangedEvent event =
                objectMapper.convertValue(envelope.getPayload(), VehicleOwnershipChangedEvent.class);

        overviewRepo.findById(event.getVin()).ifPresent(overview -> {
            overview.setCustomerId(event.getNewCustomerId());
            overviewRepo.save(overview);
        });

        String oldId = event.getOldCustomerId() + ":" + event.getVin();
        garageRepo.deleteById(oldId);

        CustomerGarageViewEntity garage = new CustomerGarageViewEntity();
        garage.setId(event.getNewCustomerId() + ":" + event.getVin());
        garage.setCustomerId(event.getNewCustomerId());
        garage.setVin(event.getVin());
        overviewRepo.findById(event.getVin()).ifPresent(overview -> {
            garage.setBrand(overview.getBrand());
            garage.setModel(overview.getModel());
        });
        garageRepo.save(garage);
    }
}
