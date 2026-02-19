\connect customer_db

CREATE TABLE customer (
    customer_number VARCHAR(20) PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) UNIQUE NOT NULL,
    phone           VARCHAR(20),
    dob             DATE,
    country         VARCHAR(100),
    status          VARCHAR(20) DEFAULT 'ACTIVE',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_address (
    id              BIGSERIAL PRIMARY KEY,
    customer_number VARCHAR(20) NOT NULL,
    address_line    VARCHAR(255),
    city            VARCHAR(100),
    country         VARCHAR(100),
    postal_code     VARCHAR(20),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_number)
        REFERENCES customer(customer_number)
);

-- Insert Customers
INSERT INTO customer (customer_number, first_name, last_name, email, phone, dob, country, status)
VALUES
('CUST-100001', 'Vamsi', 'Mohan', 'vamsi.mohan@email.com', '9876543210', '1993-05-12', 'India', 'ACTIVE'),

('CUST-100002', 'Rahul', 'Sharma', 'rahul.sharma@email.com', '9123456780', '1990-08-20', 'India', 'ACTIVE'),

('CUST-100003', 'Emma', 'Johnson', 'emma.johnson@email.com', '447700900123', '1988-03-15', 'UK', 'ACTIVE');

-- Addresses for Vamsi
INSERT INTO customer_address (customer_number, address_line, city, country, postal_code)
VALUES
('CUST-100001', '12 MG Road', 'Hyderabad', 'India', '500081'),
('CUST-100001', '45 Jubilee Hills', 'Hyderabad', 'India', '500033');

-- Address for Rahul
INSERT INTO customer_address (customer_number, address_line, city, country, postal_code)
VALUES
('CUST-100002', '22 Park Street', 'Mumbai', 'India', '400001');

-- Address for Emma
INSERT INTO customer_address (customer_number, address_line, city, country, postal_code)
VALUES
('CUST-100003', '221B Baker Street', 'London', 'UK', 'NW16XE');
