package com.autotech.maintenance.query.messaging.events;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MaintenanceScheduledEvent {

    private String appointmentId;
    private String vin;
    private String customerId;
    private String workshopId;
    private LocalDate date;
    private String slot;
    private String status;
    private String reason;
    private List<String> requestedServices;
}
