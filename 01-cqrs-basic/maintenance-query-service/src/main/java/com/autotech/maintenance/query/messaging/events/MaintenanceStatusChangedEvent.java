package com.autotech.maintenance.query.messaging.events;

import lombok.Data;

@Data
public class MaintenanceStatusChangedEvent {

    private String appointmentId;
    private String newStatus;
    private String cancelReason;
}
