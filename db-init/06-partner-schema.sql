\connect partner_db;

-----------------------------
-- PARTNER MASTER TABLE
-----------------------------

CREATE TABLE partner_master (
    partner VARCHAR(100) PRIMARY KEY,
    partner_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-----------------------------
-- ACCRUAL RULE TABLE
-----------------------------

CREATE TABLE partner_accrual_rule (
    rule_id VARCHAR(20) PRIMARY KEY,
    partner VARCHAR(100) NOT NULL,
    point_type VARCHAR(50) NOT NULL,
    points_per_unit DOUBLE PRECISION NOT NULL,
    unit_type VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_accrual_partner
        FOREIGN KEY (partner)
        REFERENCES partner_master(partner),

    CONSTRAINT unique_partner_accrual
        UNIQUE (partner, point_type, unit_type)
);

-----------------------------
-- REDEMPTION RULE TABLE
-----------------------------

CREATE TABLE partner_redemption_rule (
    rule_id VARCHAR(20) PRIMARY KEY,
    partner VARCHAR(100) NOT NULL,
    point_type VARCHAR(50) NOT NULL,
    points_required BIGINT NOT NULL,
    reward_type VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_redemption_partner
        FOREIGN KEY (partner)
        REFERENCES partner_master(partner),

    CONSTRAINT unique_partner_redemption
        UNIQUE (partner, point_type, reward_type)
);

INSERT INTO partner_master (partner, partner_type, status)
VALUES
('SkyJet Airlines', 'AIR', 'ACTIVE'),
('GrandStay Hotels', 'HOTEL', 'ACTIVE'),
('FlyHigh Bank Card', 'CARD', 'ACTIVE');

-- SkyJet Airlines: 1 mile = 1 AWARD point
INSERT INTO partner_accrual_rule
(rule_id, partner, point_type, points_per_unit, unit_type, active)
VALUES
('RUL-10001', 'SkyJet Airlines', 'AWARD', 1.0, 'MILES', true);

-- SkyJet Airlines: 1 mile = 1 TIER point
INSERT INTO partner_accrual_rule
(rule_id, partner, point_type, points_per_unit, unit_type, active)
VALUES
('RUL-10002', 'SkyJet Airlines', 'TIER', 1.0, 'MILES', true);

-- Hotel: 1 currency = 2 AWARD points
INSERT INTO partner_accrual_rule
(rule_id, partner, point_type, points_per_unit, unit_type, active)
VALUES
('RUL-20001', 'GrandStay Hotels', 'AWARD', 2.0, 'AMOUNT', true);

-- Credit Card: 1 currency = 1 AWARD point
INSERT INTO partner_accrual_rule
(rule_id, partner, point_type, points_per_unit, unit_type, active)
VALUES
('RUL-30001', 'FlyHigh Bank Card', 'AWARD', 1.0, 'AMOUNT', true);

-- Airline Upgrade
INSERT INTO partner_redemption_rule
(rule_id, partner, point_type, points_required, reward_type, active)
VALUES
('RUL-40001', 'SkyJet Airlines', 'AWARD', 10000, 'UPGRADE', true);

-- Hotel Free Night
INSERT INTO partner_redemption_rule
(rule_id, partner, point_type, points_required, reward_type, active)
VALUES
('RUL-50001', 'GrandStay Hotels', 'AWARD', 20000, 'FREE_NIGHT', true);

-- Voucher
INSERT INTO partner_redemption_rule
(rule_id, partner, point_type, points_required, reward_type, active)
VALUES
('RUL-60001', 'FlyHigh Bank Card', 'AWARD', 5000, 'VOUCHER', true);