package com.autotech.maintenance.saga.controller.dto;

public record VehicleReserveCommandPayloadDto(
        String sagaId,
        String vehicleVin,
        String customerId
) {}
