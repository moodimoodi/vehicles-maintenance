package com.autotech.maintenance.command.exception;

public class MaintenanceBusinessException extends RuntimeException {

    private final String code;

    public MaintenanceBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
