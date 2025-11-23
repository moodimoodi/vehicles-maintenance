package com.autotech.maintenance.query.messaging.events;

import lombok.Data;

@Data
public class MaintenanceCompletedEvent {

    private String appointmentId;
    private int finalMileage;
}
