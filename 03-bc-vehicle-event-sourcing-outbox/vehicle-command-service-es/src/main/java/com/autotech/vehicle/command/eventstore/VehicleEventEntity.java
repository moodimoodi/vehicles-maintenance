package com.autotech.vehicle.command.eventstore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "vehicle_events",
       uniqueConstraints = @UniqueConstraint(name = "uk_vehicle_aggregate_version", columnNames = {"aggregate_id", "version"}))
@Getter
@Setter
public class VehicleEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "event_type", nullable = false, length = 150)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private String payload;

    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}
