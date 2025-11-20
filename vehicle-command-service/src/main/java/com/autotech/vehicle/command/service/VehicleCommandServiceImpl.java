package com.autotech.vehicle.command.service;

import com.autotech.vehicle.command.dto.VehicleOwnershipChangeRequestDto;
import com.autotech.vehicle.command.dto.VehicleRegisterRequestDto;
import com.autotech.vehicle.command.dto.VehicleResponseDto;
import com.autotech.vehicle.command.dto.VehicleUpdateRequestDto;
import com.autotech.vehicle.command.entity.VehicleEntity;
import com.autotech.vehicle.command.mapper.VehicleMapper;

import com.autotech.maintenance.core.messaging.DomainEventEnvelope;
import com.autotech.vehicle.command.messaging.DomainEventPublisher;
import com.autotech.vehicle.command.messaging.events.VehicleOwnershipChangedEvent;
import com.autotech.vehicle.command.messaging.events.VehicleRegisteredEvent;
import com.autotech.vehicle.command.messaging.events.VehicleUpdatedEvent;
import com.autotech.vehicle.command.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper mapper;
    private final DomainEventPublisher eventPublisher;

    private static final String TOPIC_REGISTERED = "vehicle-registered";
    private static final String TOPIC_STATUS_UPDATED = "vehicle-updated";
    private static final String TOPIC_OWNERSHIP_CHANGED = "vehicle-ownership-changed";

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository,
                                     VehicleMapper mapper,
                                     DomainEventPublisher eventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public VehicleResponseDto registerVehicle(VehicleRegisterRequestDto request) {

        // Only one Vehicle with teh registrationn Number VIN can be registered..!!
        if (vehicleRepository.existsById(request.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN " + request.getVin() + " already exists");
        }

        VehicleEntity entity = mapper.toEntity(request);
        VehicleEntity saved = vehicleRepository.save(entity);

        VehicleRegisteredEvent event = new VehicleRegisteredEvent();
        event.setVin(saved.getVin());
        event.setBrand(saved.getBrand());
        event.setModel(saved.getModel());
        event.setYearOfProduction(saved.getYearOfProduction());
        event.setColor(saved.getColor());
        event.setCurrentMileage(saved.getCurrentMileage());
        event.setCustomerId(saved.getCustomerId());
        event.setRegistrationDate(saved.getRegistrationDate());

        DomainEventEnvelope<VehicleRegisteredEvent> envelope =
                DomainEventEnvelope.<VehicleRegisteredEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(saved.getVin())
                        .aggregateType("Vehicle")
                        .eventType("VehicleRegistered")
                        .occurredAt(Instant.now())
                        .payload(event)
                        .build();

        eventPublisher.publish(TOPIC_REGISTERED, envelope);

        return mapper.toDto(saved);
    }

    /**
     * Request for changing the vehicle Color or mileage or both
     * @param vin
     * @param request
     * @return VehicleResponse
     */
    @Override
    public VehicleResponseDto updateVehicle(String vin, VehicleUpdateRequestDto request) {
        VehicleEntity entity = vehicleRepository.findById(vin)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for VIN " + vin));

        if (request.getColor() != null) {
            entity.setColor(request.getColor());
        }
        if (request.getCurrentMileage() != null) {
            entity.setCurrentMileage(request.getCurrentMileage());
        }

        VehicleEntity saved = vehicleRepository.save(entity);

        VehicleUpdatedEvent event = new VehicleUpdatedEvent();
        event.setVin(saved.getVin());
        event.setColor(saved.getColor());
        event.setCurrentMileage(saved.getCurrentMileage());

        DomainEventEnvelope<VehicleUpdatedEvent> envelope =
                DomainEventEnvelope.<VehicleUpdatedEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(saved.getVin())
                        .aggregateType("Vehicle")
                        .eventType("VehicleUpdated")
                        .occurredAt(Instant.now())
                        .payload(event)
                        .build();

        eventPublisher.publish(TOPIC_STATUS_UPDATED, envelope);

        return mapper.toDto(saved);
    }

    /**
     * Request for changing the vehicle ownerShip
     * @param vin
     * @param request
     * @return VehicleResponse
     */
    @Override
    public VehicleResponseDto changeOwnership(String vin, VehicleOwnershipChangeRequestDto request) {
        VehicleEntity entity = vehicleRepository.findById(vin)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for VIN " + vin));

        String oldCustomerId = entity.getCustomerId();
        entity.setCustomerId(request.getNewCustomerId());
        VehicleEntity saved = vehicleRepository.save(entity);

        VehicleOwnershipChangedEvent event = new VehicleOwnershipChangedEvent();
        event.setVin(saved.getVin());
        event.setOldCustomerId(oldCustomerId);
        event.setNewCustomerId(saved.getCustomerId());

        DomainEventEnvelope<VehicleOwnershipChangedEvent> envelope =
                DomainEventEnvelope.<VehicleOwnershipChangedEvent>builder()
                        .eventId(UUID.randomUUID().toString())
                        .aggregateId(saved.getVin())
                        .aggregateType("Vehicle")
                        .eventType("VehicleOwnershipChanged")
                        .occurredAt(Instant.now())
                        .payload(event)
                        .build();

        eventPublisher.publish(TOPIC_OWNERSHIP_CHANGED, envelope);

        return mapper.toDto(saved);
    }
}
