package com.autotech.maintenance.core.messaging;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
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
