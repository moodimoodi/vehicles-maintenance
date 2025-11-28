CREATE DATABASE IF NOT EXISTS maintenance_saga_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE maintenance_saga_db;

CREATE TABLE IF NOT EXISTS saga_events (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    saga_id     VARCHAR(100) NOT NULL,
    saga_type   VARCHAR(100) NOT NULL,
    version     INT NOT NULL,
    event_type  VARCHAR(150) NOT NULL,
    payload     JSON NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_saga_aggregate_version (saga_id, version),
    INDEX idx_saga_id (saga_id),
    INDEX idx_saga_occurred_at (occurred_at)
);

CREATE TABLE IF NOT EXISTS saga_outbox_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    saga_id         VARCHAR(100) NOT NULL,
    saga_type       VARCHAR(100) NOT NULL,
    topic           VARCHAR(200) NOT NULL,
    key_id          VARCHAR(200) NOT NULL,
    payload         JSON NOT NULL,
    status          VARCHAR(30) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_attempt_at TIMESTAMP NULL,
    error_message   TEXT NULL,
    INDEX idx_saga_outbox_status_created (status, created_at)
);
