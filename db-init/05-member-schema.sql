\connect member_db

CREATE TABLE member (
    membership_number VARCHAR(20) PRIMARY KEY,
    customer_number   VARCHAR(20) NOT NULL,
    tier_level        VARCHAR(20) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    enrolled_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE point_master (
    point_type VARCHAR(20) PRIMARY KEY,
    is_tier_qualifying BOOLEAN DEFAULT FALSE,
    expiry_months INT,
    max_limit BIGINT
);

CREATE TABLE member_points_balance (
    membership_number VARCHAR(20) NOT NULL,
    point_type        VARCHAR(20) NOT NULL,
    balance           BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (membership_number, point_type),

    CONSTRAINT fk_balance_member
        FOREIGN KEY (membership_number)
        REFERENCES member(membership_number),

    CONSTRAINT fk_balance_point_type
        FOREIGN KEY (point_type)
        REFERENCES point_master(point_type)
);

CREATE TABLE points_ledger (
    ledger_id         BIGSERIAL PRIMARY KEY,
    membership_number VARCHAR(20) NOT NULL,
    point_type        VARCHAR(20) NOT NULL,
    transaction_type  VARCHAR(20) NOT NULL,
    points            BIGINT NOT NULL,
    reference_id      VARCHAR(50),
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_member
        FOREIGN KEY (membership_number)
        REFERENCES member(membership_number),

    CONSTRAINT fk_ledger_point_type
        FOREIGN KEY (point_type)
        REFERENCES point_master(point_type)
);

INSERT INTO point_master (point_type, is_tier_qualifying, expiry_months, max_limit)
VALUES
('AWARD', FALSE, 24, NULL),
('TIER', TRUE, 12, NULL);

INSERT INTO member (
    membership_number,
    customer_number,
    tier_level,
    status,
    enrolled_date
)
VALUES
('MEM-100001', 'CUST-100001', 'GOLD', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '200 days'),

('MEM-100002', 'CUST-100002', 'SILVER', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '120 days'),

('MEM-100003', 'CUST-100003', 'BASIC', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days');

INSERT INTO member_points_balance (membership_number, point_type, balance)
VALUES
-- Vamsi (Gold)
('MEM-100001', 'AWARD', 45000),
('MEM-100001', 'TIER', 60000),

-- Rahul (Silver)
('MEM-100002', 'AWARD', 18000),
('MEM-100002', 'TIER', 25000),

-- Emma (Basic)
('MEM-100003', 'AWARD', 5000),
('MEM-100003', 'TIER', 7000);

INSERT INTO points_ledger (
    membership_number,
    point_type,
    transaction_type,
    points,
    reference_id
)
VALUES
-- Vamsi accrual
('MEM-100001', 'AWARD', 'ACCRUAL', 20000, 'FLIGHT-TXN-001'),
('MEM-100001', 'TIER', 'ACCRUAL', 25000, 'FLIGHT-TXN-001'),

-- Rahul accrual
('MEM-100002', 'AWARD', 'ACCRUAL', 10000, 'HOTEL-TXN-002'),
('MEM-100002', 'TIER', 'ACCRUAL', 12000, 'HOTEL-TXN-002'),

-- Emma redemption
('MEM-100003', 'AWARD', 'REDEMPTION', -3000, 'REDEEM-TXN-003');
