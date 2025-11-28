package com.autotech.maintenance.saga.messaging.replies;

public record MaintenanceScheduleReplyMessage(
        String sagaId,
        boolean success,
        String reason
) {}
