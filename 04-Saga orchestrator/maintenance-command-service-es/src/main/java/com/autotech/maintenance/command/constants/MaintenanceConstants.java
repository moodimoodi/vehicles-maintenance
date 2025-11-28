package com.autotech.maintenance.command.constants;

public final class MaintenanceConstants {

    private MaintenanceConstants() {}

    public static final String STATUS_SCHEDULED   = "SCHEDULED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public static final String ERROR_APPOINTMENT_NOT_FOUND = "MAINTENANCE_404";
    public static final String ERROR_BUSINESS_RULE_VIOLATION = "MAINTENANCE_409";

    public static final String KAFKA_TOPIC_MAINTENANCE_EVENTS_NAME = "maintenance-events";

    public static final String OUTBOX_EVENT_PENDING_STATUS = "PENDING";

    public static final String OUTBOX_EVENT_SENT_STATUS = "SENT";
}
