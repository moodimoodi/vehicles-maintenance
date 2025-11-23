package com.autotech.vehicle.command.domain.aggregate;

import com.autotech.vehicle.command.domain.event.*;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
    private String status;
    private String ownerId;
    private int version;

    private final List<Object> uncommittedEvents = new ArrayList<>();

    public static Vehicle registerNew(String vin,
                                      String brand,
                                      String model,
                                      Integer year,
                                      Integer mileage,
                                      String status,
                                      String ownerId) {
        Vehicle v = new Vehicle();
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
        VehicleRegisteredEvent event = new VehicleRegisteredEvent(
                vin, brand, model, year, mileage, status, ownerId
        );
        v.apply(event);
        v.uncommittedEvents.add(event);
        return v;
    }

    public void updateDetails(String brand,
                              String model,
                              Integer year,
                              Integer mileage) {
        VehicleDetailsUpdatedEvent event = new VehicleDetailsUpdatedEvent(
                this.vin, brand, model, year, mileage
        );
        apply(event);
        uncommittedEvents.add(event);
    }

    public void changeStatus(String newStatus) {
        if (this.status != null && this.status.equals(newStatus)) {
            return;
        }
        VehicleStatusChangedEvent event = new VehicleStatusChangedEvent(
                this.vin, this.status, newStatus
        );
        apply(event);
        uncommittedEvents.add(event);
    }

    public void assignOwner(String ownerId) {
        VehicleOwnerAssignedEvent event = new VehicleOwnerAssignedEvent(this.vin, ownerId);
        apply(event);
        uncommittedEvents.add(event);
    }

    public void unassignOwner() {
        if (this.ownerId == null) return;
        VehicleOwnerUnassignedEvent event = new VehicleOwnerUnassignedEvent(this.vin);
        apply(event);
        uncommittedEvents.add(event);
    }

    public void replay(List<Object> history) {
        history.forEach(this::applyEvent);
        this.uncommittedEvents.clear();
    }

    private void applyEvent(Object event) {
        if (event instanceof VehicleRegisteredEvent e) apply(e);
        else if (event instanceof VehicleDetailsUpdatedEvent e) apply(e);
        else if (event instanceof VehicleStatusChangedEvent e) apply(e);
        else if (event instanceof VehicleOwnerAssignedEvent e) apply(e);
        else if (event instanceof VehicleOwnerUnassignedEvent e) apply(e);
        else throw new IllegalArgumentException("Unknown event type " + event.getClass());
    }

    private void apply(VehicleRegisteredEvent e) {
        this.vin = e.vin();
        this.brand = e.brand();
        this.model = e.model();
        this.year = e.year();
        this.mileage = e.mileage();
        this.status = e.status();
        this.ownerId = e.ownerId();
        this.version++;
    }

    private void apply(VehicleDetailsUpdatedEvent e) {
        this.brand = e.brand();
        this.model = e.model();
        this.year = e.year();
        this.mileage = e.mileage();
        this.version++;
    }

    private void apply(VehicleStatusChangedEvent e) {
        this.status = e.newStatus();
        this.version++;
    }

    private void apply(VehicleOwnerAssignedEvent e) {
        this.ownerId = e.ownerId();
        this.version++;
    }

    private void apply(VehicleOwnerUnassignedEvent e) {
        this.ownerId = null;
        this.version++;
    }

    public List<Object> getUncommittedEvents() {
        return List.copyOf(uncommittedEvents);
    }

    public void markEventsCommitted() {
        uncommittedEvents.clear();
    }

    public int getVersion() {
        return version;
    }

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public String getStatus() {
        return status;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
