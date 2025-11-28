package com.autotech.maintenance.saga.domain.saga;

public enum SagaStatus {
    STARTED,
    VEHICLE_RESERVED,
    VEHICLE_RESERVATION_FAILED,
    MAINTENANCE_SCHEDULED,
    MAINTENANCE_SCHEDULE_FAILED,
    COMPLETED,
    COMPENSATED
}
