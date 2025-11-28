package com.autotech.maintenance.saga.messaging.replies;

public record VehicleReserveReplyMessage(
        String sagaId,
        boolean success,
        String reason
) {}
