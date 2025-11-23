package com.autotech.vehicle.command.dto;

import lombok.Data;

@Data
public class VehicleUpdateDetailsRequest {
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
}
