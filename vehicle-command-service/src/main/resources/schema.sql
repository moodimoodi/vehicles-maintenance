CREATE TABLE IF NOT EXISTS vehicles (
    vin                 VARCHAR(17) PRIMARY KEY,
    brand               VARCHAR(100) NOT NULL,
    model               VARCHAR(100) NOT NULL,
    year_of_production  INT,
    color               VARCHAR(50),
    current_mileage     INT,
    customer_id         VARCHAR(255) NOT NULL,
    registration_date   DATE
);
