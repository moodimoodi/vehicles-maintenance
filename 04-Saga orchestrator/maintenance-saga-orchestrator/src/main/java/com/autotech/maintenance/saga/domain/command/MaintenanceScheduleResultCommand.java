package com.autotech.maintenance.saga.domain.command;

public record MaintenanceScheduleResultCommand(
        String sagaId,
        boolean success,
        String reason
) implements SagaCommand {
}
