package com.autotech.vehicle.query.messaging.events;

import lombok.Data;

@Data
public class VehicleUpdatedEvent {

    private String vin;
    private String color;
    private Integer currentMileage;
}
