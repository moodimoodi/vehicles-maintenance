package com.autotech.vehicle.command.messaging.events;

import lombok.Data;

@Data
public class VehicleUpdatedEvent {

    private String vin;
    private String color;
    private Integer currentMileage;
}
