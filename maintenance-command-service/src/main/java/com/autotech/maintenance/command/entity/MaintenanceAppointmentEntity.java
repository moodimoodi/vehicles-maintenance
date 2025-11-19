package com.autotech.maintenance.command.entity;

import com.autotech.maintenance.command.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maintenance_appointments")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceAppointmentEntity extends Auditable {

    @Id
    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "vin", nullable = false)
    private String vin;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "workshop_id", nullable = false)
    private String workshopId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "slot", nullable = false)
    private String slot;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reason")
    private String reason;

    @ElementCollection
    @CollectionTable(name = "maintenance_services", joinColumns = @JoinColumn(name = "appointment_id"))
    @Column(name = "service_code")
    private List<String> requestedServices = new ArrayList<>();


}
