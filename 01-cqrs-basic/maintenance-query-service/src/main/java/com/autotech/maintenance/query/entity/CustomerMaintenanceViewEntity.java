package com.autotech.maintenance.query.entity;

import com.autotech.maintenance.query.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "customer_maintenance_view")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerMaintenanceViewEntity extends Auditable {

    @Id
    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "vin", nullable = false)
    private String vin;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "workshop_id", nullable = false)
    private String workshopId;

    @Column(name = "workshop_name")
    private String workshopName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "slot", nullable = false)
    private String slot;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reason")
    private String reason;
}
