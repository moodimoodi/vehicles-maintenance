package com.autotech.maintenance.command.exception;

public class MaintenanceNotFoundException extends RuntimeException {

    private final String appointmentId;

    public MaintenanceNotFoundException(String appointmentId) {
        super("Maintenance appointment not found: " + appointmentId);
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
}
