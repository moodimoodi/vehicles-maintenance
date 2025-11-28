package com.autotech.maintenance.saga.controller.dto;

import com.autotech.maintenance.saga.domain.saga.SagaStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SagaStateDTO {

    private String sagaId;
    private String appointmentId;
    private String vehicleVin;
    private String customerId;
    private String workshopId;
    private LocalDateTime scheduledAt;
    private String maintenanceType;

    private SagaStatus status;
}
