package com.autotech.maintenance.saga.domain.saga.events;

public record VehicleReservationFailedEvent(String sagaId, String reason) {}
