package com.autotech.maintenance.saga.eventstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "saga_outbox_events",
       indexes = @Index(name = "idx_saga_outbox_status_created", columnList = "status, created_at"))
@Getter
@Setter
public class SagaOutboxEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_id", nullable = false, length = 100)
    private String sagaId;

    @Column(name = "saga_type", nullable = false, length = 100)
    private String sagaType;

    @Column(name = "topic", nullable = false, length = 200)
    private String topic;

    @Column(name = "key_id", nullable = false, length = 200)
    private String key_id;

    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private String payload;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "error_message")
    private String errorMessage;
}
