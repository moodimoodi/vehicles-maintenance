package com.autotech.maintenance.saga.messaging.commands;

import lombok.NoArgsConstructor;

public record VehicleReserveCommandMessage(
        String sagaId,
        String vehicleVin,
        String customerId
) {}
