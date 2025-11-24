package com.autotech.vehicle.command.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "vehicles")
@Data
public class VehicleEntity {

    @Id
    @Column(name = "vin", length = 17)
    private String vin;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year_of_production")
    private Integer yearOfProduction;

    @Column(name = "color")
    private String color;

    @Column(name = "current_mileage")
    private Integer currentMileage;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "registration_date")
    private LocalDate registrationDate;
}
