CREATE TABLE IF NOT EXISTS customer_maintenance_view (
    appointment_id   VARCHAR(255) PRIMARY KEY,
    customer_id      VARCHAR(255) NOT NULL,
    vin              VARCHAR(17)  NOT NULL,
    vehicle_model    VARCHAR(255),
    workshop_id      VARCHAR(255) NOT NULL,
    workshop_name    VARCHAR(255),
    date             DATE         NOT NULL,
    slot             VARCHAR(50)  NOT NULL,
    status           VARCHAR(50)  NOT NULL,
    reason           VARCHAR(255),
    created_at       DATETIME,
    updated_at       DATETIME
);

CREATE INDEX IF NOT EXISTS idx_customer_maintenance_view_customer
    ON customer_maintenance_view (customer_id, date);

CREATE TABLE IF NOT EXISTS workshop_daily_planning_view (
    appointment_id   VARCHAR(255) PRIMARY KEY,
    workshop_id      VARCHAR(255) NOT NULL,
    workshop_name    VARCHAR(255),
    date             DATE         NOT NULL,
    slot             VARCHAR(50)  NOT NULL,
    vin              VARCHAR(17)  NOT NULL,
    vehicle_model    VARCHAR(255),
    status           VARCHAR(50)  NOT NULL,
    bay              VARCHAR(50),
    created_at       DATETIME,
    updated_at       DATETIME
);

CREATE INDEX IF NOT EXISTS idx_workshop_planning_view
    ON workshop_daily_planning_view (workshop_id, date, slot);
