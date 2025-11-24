package com.autotech.vehicle.command.domain.aggregate;

import com.autotech.vehicle.command.domain.event.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain aggregate of Vehicle
 */
@Data
public class VehicleAggregate extends AggregateRoot{

    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
    private String status;
    private String ownerId;
    public static VehicleAggregate registerNew(String vin,
                                               String brand,
                                               String model,
                                               Integer year,
                                               Integer mileage,
                                               String status,
                                               String ownerId) {
        VehicleAggregate agg = new VehicleAggregate();
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
        VehicleRegisteredEvent event = new VehicleRegisteredEvent(
                vin, brand, model, year, mileage, status, ownerId
        );
        // Applicate the event state on the aggregate
        agg.applyChange(event);
        return agg;
    }

    public void updateDetails(String brand,
                              String model,
                              Integer year,
                              Integer mileage) {
        VehicleDetailsUpdatedEvent event = new VehicleDetailsUpdatedEvent(
                this.vin, brand, model, year, mileage
        );
        // Applicate the event state on the aggregate
        applyChange(event);
    }

    public void changeStatus(String newStatus) {
        if (this.status != null && this.status.equals(newStatus)) {
            return;
        }
        VehicleStatusChangedEvent event = new VehicleStatusChangedEvent(
                this.vin, this.status, newStatus
        );
        applyChange(event);
    }

    public void assignOwner(String ownerId) {
        VehicleOwnerAssignedEvent event = new VehicleOwnerAssignedEvent(this.vin, ownerId);
        apply(event);
        applyChange(event);
    }

    public void unassignOwner() {
        if (this.ownerId == null) return;
        VehicleOwnerUnassignedEvent event = new VehicleOwnerUnassignedEvent(this.vin);
        apply(event);
        applyChange(event);
    }

    /**
     * Internal mutate of the state
     * @param event
     */
    @Override
    protected void mutate(Object event) {
       if (event instanceof VehicleDetailsUpdatedEvent e) {
            apply(e);
        } else if (event instanceof VehicleOwnerAssignedEvent e) {
            apply(e);
        } else if (event instanceof VehicleOwnerUnassignedEvent e) {
            apply(e);
        } else if (event instanceof VehicleRegisteredEvent e) {
            apply(e);
        } else if (event instanceof VehicleStatusChangedEvent e) {
            apply(e);
        }
        else {
            throw new IllegalArgumentException("Unknown event: " + event.getClass());
        }
    }

    public void replay(List<Object> history) {
        history.forEach(this::applyEvent);
        markEventsCommitted();
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
        incrementVersion();
    }

    private void apply(VehicleDetailsUpdatedEvent e) {
        this.brand = e.brand();
        this.model = e.model();
        this.year = e.year();
        this.mileage = e.mileage();
        incrementVersion();
    }

    private void apply(VehicleStatusChangedEvent e) {
        this.status = e.newStatus();
        incrementVersion();
    }

    private void apply(VehicleOwnerAssignedEvent e) {
        this.ownerId = e.ownerId();
        incrementVersion();
    }

    private void apply(VehicleOwnerUnassignedEvent e) {
        this.ownerId = null;
        incrementVersion();
    }

}
