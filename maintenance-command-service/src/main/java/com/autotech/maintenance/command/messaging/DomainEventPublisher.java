package com.autotech.maintenance.command.messaging;

import com.autotech.maintenance.core.messaging.DomainEventEnvelope;

public interface DomainEventPublisher {

    <T> void publish(String topic, DomainEventEnvelope<T> envelope);
}
