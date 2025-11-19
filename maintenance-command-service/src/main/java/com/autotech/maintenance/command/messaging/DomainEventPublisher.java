package com.autotech.maintenance.command.messaging;

public interface DomainEventPublisher {

    <T> void publish(String topic, DomainEventEnvelope<T> envelope);
}
