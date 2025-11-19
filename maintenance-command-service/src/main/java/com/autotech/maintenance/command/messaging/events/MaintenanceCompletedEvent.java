package com.autotech.maintenance.command.messaging.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaintenanceCompletedEvent {

    private String appointmentId;
    private int finalMileage;
}
