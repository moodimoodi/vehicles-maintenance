package com.autotech.maintenance.saga.constants;

public final class SagaConstants {

    private SagaConstants() {}

    public static final String STATUS_SCHEDULED   = "SCHEDULED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public static final String ERROR_APPOINTMENT_NOT_FOUND = "MAINTENANCE_404";
    public static final String ERROR_BUSINESS_RULE_VIOLATION = "MAINTENANCE_409";

    // OUTBOX EVENTS
    public static final String OUTBOX_SAGA_EVENT_PENDING_STATUS = "PENDING";

    public static final String OUTBOX_SAGA_EVENT_SENT_STATUS = "SENT";

    public static final String OUTBOX_SAGA_EVENT_FAILED_STATUS = "FAILED";

    public static final String OUTBOX_SAGA_BOOKING_MAINTENANCE_EVENT = "MaintenanceBookingSaga";

    // Kafka
    public static final String KAFKA_TOPIC_SAGA_MAINTENANCE_EVENTS_NAME = "maintenance-events";

    public static final String KAFKA_TOPIC_SAGA_VEHICLE_EVENTS_NAME = "vehicle-commands";
}
