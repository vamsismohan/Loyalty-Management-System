\connect auth_db;

INSERT INTO roles (role_name) VALUES
('ROLE_ADMIN'),
('ROLE_SUPPORT'),
('ROLE_PARTNER'),
('ROLE_MEMBER');

INSERT INTO privileges (privilege_name) VALUES
('CREATE_PARTNER'),
('VIEW_MEMBER'),
('UPDATE_POINTS'),
('REDEEM_POINTS');

INSERT INTO users (username, email, password, status, created_at)
VALUES ('admin', 'admin@test.com',
'$2a$10$b2czDFU2BDn0weaD5p2PZePwcgRoXrc879kJQnpGZPSB6zeQAJnDS',
'ACTIVE', now());

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

