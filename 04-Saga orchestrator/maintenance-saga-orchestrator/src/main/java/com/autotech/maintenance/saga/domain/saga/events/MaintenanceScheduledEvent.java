package com.autotech.maintenance.saga.domain.saga.events;

public record MaintenanceScheduledEvent(String sagaId, String appointmentId) {}
