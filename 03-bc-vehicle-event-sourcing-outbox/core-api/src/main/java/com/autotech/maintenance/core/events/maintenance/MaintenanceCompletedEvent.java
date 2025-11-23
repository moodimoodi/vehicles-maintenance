package com.autotech.maintenance.core.events.maintenance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaintenanceCompletedEvent {

    private String appointmentId;
    private int finalMileage;
}
