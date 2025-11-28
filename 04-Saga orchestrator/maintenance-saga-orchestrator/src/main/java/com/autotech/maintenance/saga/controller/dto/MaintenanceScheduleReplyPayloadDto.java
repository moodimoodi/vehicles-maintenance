package com.autotech.maintenance.saga.controller.dto;

public record MaintenanceScheduleReplyPayloadDto(
        String sagaId,
        boolean success,
        String reason
) {}
