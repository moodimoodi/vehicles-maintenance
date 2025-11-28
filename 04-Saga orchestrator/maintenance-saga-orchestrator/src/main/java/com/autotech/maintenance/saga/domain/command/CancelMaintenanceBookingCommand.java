package com.autotech.maintenance.saga.domain.command;

public record CancelMaintenanceBookingCommand(
        String sagaId,
        String reason
) implements SagaCommand {
}
