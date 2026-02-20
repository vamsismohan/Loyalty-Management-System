\connect member_db

CREATE TABLE member (
    membership_number VARCHAR(20) PRIMARY KEY,
    customer_number   VARCHAR(20) NOT NULL,
    tier_level        VARCHAR(20) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    enrolled_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE point_type (
    point_type_id BIGSERIAL PRIMARY KEY,
    point_type_name VARCHAR(50) NOT NULL,
    is_tier_qualifying BOOLEAN DEFAULT FALSE,
    expiry_months INT,
    max_limit BIGINT
);

CREATE TABLE member_points_balance (
    membership_number VARCHAR(20) NOT NULL,
    point_type_id     BIGINT NOT NULL,
    balance           BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (membership_number, point_type_id),

    CONSTRAINT fk_balance_member
        FOREIGN KEY (membership_number)
        REFERENCES member(membership_number),

    CONSTRAINT fk_balance_point_type
        FOREIGN KEY (point_type_id)
        REFERENCES point_type(point_type_id)
);

CREATE TABLE points_ledger (
    ledger_id         BIGSERIAL PRIMARY KEY,
    membership_number VARCHAR(20) NOT NULL,
    point_type_id     BIGINT NOT NULL,
    transaction_type  VARCHAR(20) NOT NULL, -- ACCRUAL / REDEMPTION / ADJUSTMENT
    points            BIGINT NOT NULL,
    reference_id      VARCHAR(50),
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_member
        FOREIGN KEY (membership_number)
        REFERENCES member(membership_number),

    CONSTRAINT fk_ledger_point_type
        FOREIGN KEY (point_type_id)
        REFERENCES point_type(point_type_id)
);

INSERT INTO point_type (point_type_id, point_type_name, is_tier_qualifying, expiry_months, max_limit)
VALUES
(1, 'AWARD_POINTS', FALSE, 24, NULL),
(2, 'TIER_POINTS', TRUE, 12, NULL);

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

INSERT INTO member_points_balance (membership_number, point_type_id, balance)
VALUES
-- Vamsi (Gold)
('MEM-100001', 1, 45000),
('MEM-100001', 2, 60000),

-- Rahul (Silver)
('MEM-100002', 1, 18000),
('MEM-100002', 2, 25000),

-- Emma (Basic)
('MEM-100003', 1, 5000),
('MEM-100003', 2, 7000);

INSERT INTO points_ledger (
    membership_number,
    point_type_id,
    transaction_type,
    points,
    reference_id
)
VALUES
-- Vamsi accrual
('MEM-100001', 1, 'ACCRUAL', 20000, 'FLIGHT-TXN-001'),
('MEM-100001', 2, 'ACCRUAL', 25000, 'FLIGHT-TXN-001'),

-- Rahul accrual
('MEM-100002', 1, 'ACCRUAL', 10000, 'HOTEL-TXN-002'),
('MEM-100002', 2, 'ACCRUAL', 12000, 'HOTEL-TXN-002'),

-- Emma redemption
('MEM-100003', 1, 'REDEMPTION', -3000, 'REDEEM-TXN-003');
