package com.autotech.vehicle.query.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer_garage_view")
@Data
public class CustomerGarageViewEntity {

    @Id
    @Column(name = "id")
    private String id; // customerId + ":" + vin

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "vin", nullable = false)
    private String vin;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;
}
