package com.autotech.vehicle.query.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_view")
@Data
public class VehicleViewEntity {

    @Id
    @Column(name = "vin", length = 100)
    private String vin;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "owner_id", length = 100)
    private String ownerId;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;
}
