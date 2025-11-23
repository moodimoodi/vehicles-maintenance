CREATE DATABASE IF NOT EXISTS vehicle_query_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE vehicle_query_db;

CREATE TABLE IF NOT EXISTS vehicle_view (
    vin             VARCHAR(100) PRIMARY KEY,
    brand           VARCHAR(100) NOT NULL,
    model           VARCHAR(100) NOT NULL,
    year            INT NOT NULL,
    mileage         INT NOT NULL,
    status          VARCHAR(30) NOT NULL,
    owner_id        VARCHAR(100),
    last_updated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_vehicle_view_owner ON vehicle_view(owner_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_view_status ON vehicle_view(status);
