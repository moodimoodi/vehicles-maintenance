package com.autotech.maintenance.core.events.maintenance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaintenanceStatusChangedEvent {

    private String appointmentId;
    private String newStatus;
    private String cancelReason;
}
