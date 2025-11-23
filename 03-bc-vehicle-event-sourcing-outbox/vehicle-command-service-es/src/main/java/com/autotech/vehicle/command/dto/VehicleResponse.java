package com.autotech.vehicle.command.dto;

import lombok.Data;

@Data
public class VehicleResponse {
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
    private String status;
    private String ownerId;
    private int version;
}
