\connect auth_db;

INSERT INTO privileges (privilege_name) VALUES
('CREATE_USER'),
('VIEW_USER'),
('CREATE_ROLE'),
('CREATE_PRIVILEGE'),
('ASSIGN_PRIVILEGE'),
('CREATE_MEMBER');

INSERT INTO roles (role_name) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- ROLE_ADMIN gets all privileges
INSERT INTO role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM roles r, privileges p
WHERE r.role_name = 'ROLE_ADMIN';

-- ROLE_USER limited privilege
INSERT INTO role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM roles r, privileges p
WHERE r.role_name = 'ROLE_USER'
AND p.privilege_name = 'CREATE_MEMBER';

-- Admin user
INSERT INTO users (username, email, password)
VALUES (
'admin',
'admin@loyalty.com',
'$2a$10$EtyytLaVcIYN9kPLSqBPEeZoygpWoeV3srCBcfMMot.TioYmRedGK'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
AND r.role_name = 'ROLE_ADMIN';