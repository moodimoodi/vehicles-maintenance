package com.autotech.vehicle.query.messaging;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class DomainEventEnvelope<T> {

    private String eventId;
    private String aggregateId;
    private String eventType;
    private String aggregateType;
    private Instant occurredAt;
    private String correlationId;
    private String causationId;
    private Map<String, Object> headers;
    private T payload;
}
