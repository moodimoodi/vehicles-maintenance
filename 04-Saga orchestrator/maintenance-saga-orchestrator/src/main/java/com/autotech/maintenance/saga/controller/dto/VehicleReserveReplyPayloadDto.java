package com.autotech.maintenance.saga.controller.dto;

public record VehicleReserveReplyPayloadDto(
        String sagaId,
        boolean success,
        String reason
) {}

