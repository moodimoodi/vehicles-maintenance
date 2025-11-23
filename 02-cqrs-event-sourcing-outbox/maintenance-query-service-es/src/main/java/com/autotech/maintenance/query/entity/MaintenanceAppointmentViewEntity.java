package com.autotech.maintenance.query.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_appointment_view")
@Data
public class MaintenanceAppointmentViewEntity {

    @Id
    @Column(name = "appointment_id", length = 100)
    private String appointmentId;

    @Column(name = "vehicle_vin", nullable = false, length = 50)
    private String vehicleVin;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Column(name = "workshop_id", nullable = false, length = 100)
    private String workshopId;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "maintenance_type", nullable = false, length = 100)
    private String maintenanceType;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;
}
