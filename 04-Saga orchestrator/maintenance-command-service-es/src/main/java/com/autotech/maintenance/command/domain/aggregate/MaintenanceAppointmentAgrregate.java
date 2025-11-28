package com.autotech.maintenance.command.domain.aggregate;

/**
 * Domain aggregate of maintenance
 */

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import com.autotech.maintenance.command.domain.events.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class MaintenanceAppointmentAgrregate extends AggregateRoot {

    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;
    private String status;

    public static MaintenanceAppointmentAgrregate scheduleNew(
            String appointmentId,
            String vehicleVin,
            String customerId,
            String workshopId,
            LocalDateTime scheduledAt,
            String maintenanceType
    ) {
        MaintenanceAppointmentAgrregate agg = new MaintenanceAppointmentAgrregate();
        MaintenanceScheduledEvent event = new MaintenanceScheduledEvent(
                appointmentId, vehicleVin, customerId, workshopId,
                scheduledAt, maintenanceType, MaintenanceConstants.STATUS_SCHEDULED
        );
        // Applicate the event state on the aggregate
        agg.applyChange(event);
        return agg;
    }

    public void reschedule(LocalDateTime newDateTime) {
        ensureNotCancelledOrCompleted();
        MaintenanceRescheduledEvent event = new MaintenanceRescheduledEvent(
                appointmentId, this.scheduledAt, newDateTime
        );
        // Applicate the event state on the aggregate
        applyChange(event);
    }

    public void changeStatus(String newStatus) {
        if (this.status != null && this.status.equals(newStatus)) {
            return;
        }
        MaintenanceStatusChangedEvent event = new MaintenanceStatusChangedEvent(
                appointmentId, this.status, newStatus
        );
        // Applicate the event state on the aggregate
        applyChange(event);
    }

    public void cancel(String reason) {
        if (MaintenanceConstants.STATUS_CANCELLED.equals(status)) {
            return;
        }
        MaintenanceCancelledEvent event = new MaintenanceCancelledEvent(appointmentId, reason);
        // Applicate the event state on the aggregate
        applyChange(event);
    }

    public void complete(LocalDateTime completedAt) {
        ensureNotInProgessOrNotScheduled();
        MaintenanceCompletedEvent event = new MaintenanceCompletedEvent(appointmentId, completedAt);
        // Applicate the event state on the aggregate
        applyChange(event);
    }

    /**
     * Internal mutate of the state
     * @param event
     */
    @Override
    protected void mutate(Object event) {
        if (event instanceof MaintenanceScheduledEvent e) {
            apply(e);
        } else if (event instanceof MaintenanceRescheduledEvent e) {
            apply(e);
        } else if (event instanceof MaintenanceStatusChangedEvent e) {
            apply(e);
        } else if (event instanceof MaintenanceCancelledEvent e) {
            apply(e);
        } else if (event instanceof MaintenanceCompletedEvent e) {
            apply(e);
        } else {
            throw new IllegalArgumentException("Unknown event: " + event.getClass());
        }
    }

    public void replay(List<Object> history) {
        history.forEach(this::applyEvent);
        markEventsCommitted();
    }

    private void applyEvent(Object event) {
        if (event instanceof MaintenanceScheduledEvent e) apply(e);
        else if (event instanceof MaintenanceRescheduledEvent e) apply(e);
        else if (event instanceof MaintenanceStatusChangedEvent e) apply(e);
        else if (event instanceof MaintenanceCancelledEvent e) apply(e);
        else if (event instanceof MaintenanceCompletedEvent e) apply(e);
        else throw new IllegalArgumentException("Unknown event type " + event.getClass());
    }

    private void apply(MaintenanceScheduledEvent e) {
        this.appointmentId = e.appointmentId();
        this.vehicleVin = e.vehicleVin();
        this.customerId = e.customerId();
        this.workshopId = e.workshopId();
        this.scheduledAt = e.scheduledAt();
        this.maintenanceType = e.maintenanceType();
        this.status = e.status();
        incrementVersion();
    }

    private void apply(MaintenanceRescheduledEvent e) {
        this.scheduledAt = e.newScheduledAt();
        incrementVersion();
    }

    private void apply(MaintenanceStatusChangedEvent e) {
        this.status = e.newStatus();
        incrementVersion();
    }

    private void apply(MaintenanceCancelledEvent e) {
        this.status = MaintenanceConstants.STATUS_CANCELLED;
        incrementVersion();
    }

    private void apply(MaintenanceCompletedEvent e) {
        this.status = MaintenanceConstants.STATUS_COMPLETED;
        incrementVersion();
    }

    // Utilities methods
    private void ensureNotCancelledOrCompleted() {
        if (MaintenanceConstants.STATUS_CANCELLED.equals(status) || MaintenanceConstants.STATUS_COMPLETED.equals(status)) {
            throw new IllegalStateException("Cannot reschedule cancelled or completed maintenance");
        }
    }

    private void ensureNotInProgessOrNotScheduled() {
        if (!MaintenanceConstants.STATUS_IN_PROGRESS.equals(status) && !MaintenanceConstants.STATUS_SCHEDULED.equals(status)) {
            throw new IllegalStateException("Cannot complete maintenance with status " + status);
        }
    }

}
