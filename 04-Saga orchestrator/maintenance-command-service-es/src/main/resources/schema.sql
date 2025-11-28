CREATE DATABASE IF NOT EXISTS maintenance_command_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE maintenance_command_db;

CREATE TABLE IF NOT EXISTS maintenance_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_id    VARCHAR(100) NOT NULL,
    aggregate_type  VARCHAR(100) NOT NULL,
    version         INT NOT NULL,
    event_type      VARCHAR(150) NOT NULL,
    payload         JSON NOT NULL,
    metadata        JSON NULL,
    occurred_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_aggregate_version (aggregate_id, version),
    INDEX idx_aggregate_id (aggregate_id),
    INDEX idx_occurred_at (occurred_at)
);

CREATE TABLE IF NOT EXISTS maintenance_outbox_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_id    VARCHAR(100) NOT NULL,
    aggregate_type  VARCHAR(100) NOT NULL,
    event_type      VARCHAR(150) NOT NULL,
    topic           VARCHAR(200) NOT NULL,
    payload         JSON NOT NULL,
    headers         JSON NULL,
    status          VARCHAR(30) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_attempt_at TIMESTAMP NULL,
    error_message   TEXT NULL,
    INDEX idx_status_created (status, created_at)
);
