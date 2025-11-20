CREATE TABLE IF NOT EXISTS vehicle_overview_view (
    vin                 VARCHAR(17) PRIMARY KEY,
    brand               VARCHAR(100) NOT NULL,
    model               VARCHAR(100) NOT NULL,
    year_of_production  INT,
    color               VARCHAR(50),
    current_mileage     INT,
    customer_id         VARCHAR(255) NOT NULL,
    registration_date   DATE
);

CREATE TABLE IF NOT EXISTS customer_garage_view (
    id          VARCHAR(300) PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    vin         VARCHAR(17) NOT NULL,
    brand       VARCHAR(100) NOT NULL,
    model       VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_customer_garage_view_customer
    ON customer_garage_view (customer_id);
