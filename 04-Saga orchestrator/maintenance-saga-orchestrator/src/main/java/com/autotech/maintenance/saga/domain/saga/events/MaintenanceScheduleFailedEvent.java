package com.autotech.maintenance.saga.domain.saga.events;

public record MaintenanceScheduleFailedEvent(String sagaId, String reason) {}
