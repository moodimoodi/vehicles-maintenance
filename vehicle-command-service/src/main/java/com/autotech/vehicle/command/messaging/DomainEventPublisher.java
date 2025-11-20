package com.autotech.vehicle.command.messaging;

import com.autotech.maintenance.core.messaging.DomainEventEnvelope;

public interface DomainEventPublisher {

    <T> void publish(String topic, DomainEventEnvelope<T> envelope);
}
