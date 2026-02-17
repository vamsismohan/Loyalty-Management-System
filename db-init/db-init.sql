CREATE DATABASE auth_db;
CREATE DATABASE customer_db;
CREATE DATABASE member_db;
CREATE DATABASE partner_db;
CREATE DATABASE accrual_db;
CREATE DATABASE redemption_db;
CREATE DATABASE notification_db;

-- Roles
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_SUPPORT');
INSERT INTO roles (role_name) VALUES ('ROLE_PARTNER');
INSERT INTO roles (role_name) VALUES ('ROLE_MEMBER');

-- Privileges
INSERT INTO privileges (privilege_name) VALUES ('CREATE_PARTNER');
INSERT INTO privileges (privilege_name) VALUES ('VIEW_MEMBER');
INSERT INTO privileges (privilege_name) VALUES ('UPDATE_POINTS');
INSERT INTO privileges (privilege_name) VALUES ('REDEEM_POINTS');