package com.autotech.maintenance.saga.domain.command;

public record VehicleReserveResultCommand(
        String sagaId,
        boolean success,
        String reason
) implements SagaCommand {
}
