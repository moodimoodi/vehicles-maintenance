package com.autotech.vehicle.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRegisterRequestDto {
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private Integer mileage;
    private String status;
    private String ownerId;
}
