CREATE DATABASE IF NOT EXISTS maintenance_query_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE maintenance_query_db;

CREATE TABLE IF NOT EXISTS maintenance_appointment_view (
    appointment_id   VARCHAR(100) PRIMARY KEY,
    vehicle_vin      VARCHAR(50) NOT NULL,
    customer_id      VARCHAR(100) NOT NULL,
    workshop_id      VARCHAR(100) NOT NULL,
    scheduled_at     TIMESTAMP NOT NULL,
    maintenance_type VARCHAR(100) NOT NULL,
    status           VARCHAR(30) NOT NULL,
    last_updated_at  TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_maintenance_view_customer ON maintenance_appointment_view(customer_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_view_workshop ON maintenance_appointment_view(workshop_id);
