package com.autotech.maintenance.saga.eventstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "saga_events",
       uniqueConstraints = @UniqueConstraint(name = "uk_saga_aggregate_version", columnNames = {"saga_id", "version"}))
@Getter
@Setter
public class SagaEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_id", nullable = false, length = 100)
    private String sagaId;

    @Column(name = "saga_type", nullable = false, length = 100)
    private String sagaType;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "event_type", nullable = false, length = 150)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private String payload;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}
