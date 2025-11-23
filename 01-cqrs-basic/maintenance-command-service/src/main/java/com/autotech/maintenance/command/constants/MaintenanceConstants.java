package com.autotech.maintenance.command.constants;

public final class MaintenanceConstants {

    private MaintenanceConstants() {}

    public static final String STATUS_CREATED   = "CREATED";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public static final String ERROR_APPOINTMENT_NOT_FOUND = "MAINTENANCE_404";
    public static final String ERROR_BUSINESS_RULE_VIOLATION = "MAINTENANCE_409";
}
