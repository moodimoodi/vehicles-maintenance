package com.autotech.maintenance.saga.controller.dto;

import com.autotech.maintenance.saga.domain.saga.SagaStatus;
import lombok.Data;

@Data
public class StartMaintenanceBookingResponseDto {
    private String sagaId;
    private String appointmentId;
    private SagaStatus status;
}
