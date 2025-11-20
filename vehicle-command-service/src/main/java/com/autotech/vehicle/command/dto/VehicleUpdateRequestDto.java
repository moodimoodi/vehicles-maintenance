package com.autotech.vehicle.command.dto;

import lombok.Data;

@Data
public class VehicleUpdateRequestDto {

    private String color;
    private Integer currentMileage;
}
