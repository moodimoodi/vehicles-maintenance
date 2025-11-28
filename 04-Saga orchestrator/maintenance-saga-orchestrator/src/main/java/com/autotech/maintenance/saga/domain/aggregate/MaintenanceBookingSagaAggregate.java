package com.autotech.maintenance.saga.domain.aggregate;

import com.autotech.maintenance.saga.domain.common.AggregateRoot;
import com.autotech.maintenance.saga.domain.saga.SagaStatus;
import com.autotech.maintenance.saga.domain.saga.events.*;

import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceBookingSagaAggregate extends AggregateRoot {

    private String sagaId;
    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
    private SagaStatus status;

    public static MaintenanceBookingSagaAggregate start(
            String sagaId,
            String appointmentId,
            String vehicleVin,
            String customerId,
            String workshopId,
            LocalDateTime scheduledAt,
            String maintenanceType
    ) {
        MaintenanceBookingSagaAggregate saga = new MaintenanceBookingSagaAggregate();
        SagaStartedEvent event = new SagaStartedEvent(
                sagaId, appointmentId, vehicleVin, customerId, workshopId, scheduledAt, maintenanceType
        );
        saga.applyChange(event);
        return saga;
    }

    public void onVehicleReserved() {
        if (status != SagaStatus.STARTED) {
            throw new IllegalStateException("Vehicle can only be reserved from STARTED, current=" + status);
        }
        applyChange(new VehicleReservedEvent(sagaId));
    }

    public void onVehicleReservationFailed(String reason) {
        if (status != SagaStatus.STARTED) {
            throw new IllegalStateException("Vehicle failure only from STARTED, current=" + status);
        }
        applyChange(new VehicleReservationFailedEvent(sagaId, reason));
    }

    public void onMaintenanceScheduled() {
        if (status != SagaStatus.VEHICLE_RESERVED) {
            throw new IllegalStateException("Maintenance only after VEHICLE_RESERVED, current=" + status);
        }
        applyChange(new MaintenanceScheduledEvent(sagaId, appointmentId));
        applyChange(new SagaCompletedEvent(sagaId));
    }

    public void onMaintenanceScheduleFailed(String reason) {
        if (status != SagaStatus.VEHICLE_RESERVED) {
            throw new IllegalStateException("Maintenance failure only after VEHICLE_RESERVED, current=" + status);
        }
        applyChange(new MaintenanceScheduleFailedEvent(sagaId, reason));
        applyChange(new SagaCompensatedEvent(sagaId));
    }

    public void replay(List<Object> history) {
        history.forEach(this::mutate);
        markEventsCommitted();
    }

    @Override
    protected void mutate(Object event) {
        if (event instanceof SagaStartedEvent e) {
            this.sagaId = e.sagaId();
            this.appointmentId = e.appointmentId();
            this.vehicleVin = e.vehicleVin();
            this.customerId = e.customerId();
            this.workshopId = e.workshopId();
            this.scheduledAt = e.scheduledAt();
            this.maintenanceType = e.maintenanceType();
            this.status = SagaStatus.STARTED;
        } else if (event instanceof VehicleReservedEvent) {
            this.status = SagaStatus.VEHICLE_RESERVED;
        } else if (event instanceof VehicleReservationFailedEvent) {
            this.status = SagaStatus.VEHICLE_RESERVATION_FAILED;
        } else if (event instanceof MaintenanceScheduledEvent) {
            this.status = SagaStatus.MAINTENANCE_SCHEDULED;
        } else if (event instanceof MaintenanceScheduleFailedEvent) {
            this.status = SagaStatus.MAINTENANCE_SCHEDULE_FAILED;
        } else if (event instanceof SagaCompletedEvent) {
            this.status = SagaStatus.COMPLETED;
        } else if (event instanceof SagaCompensatedEvent) {
            this.status = SagaStatus.COMPENSATED;
        } else {
            throw new IllegalArgumentException("Unknown event " + event.getClass());
        }
    }

    public String getSagaId() { return sagaId; }
    public String getAppointmentId() { return appointmentId; }
    public String getVehicleVin() { return vehicleVin; }
    public String getCustomerId() { return customerId; }
    public String getWorkshopId() { return workshopId; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getMaintenanceType() { return maintenanceType; }
    public SagaStatus getStatus() { return status; }
}
