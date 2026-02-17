\connect auth_db;

DROP TABLE IF EXISTS role_privileges;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS privileges;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE privileges (
    id BIGSERIAL PRIMARY KEY,
    privilege_name VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY(role_id)
        REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_role UNIQUE(user_id, role_id)
);

CREATE TABLE role_privileges (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,
    CONSTRAINT fk_role_rp FOREIGN KEY(role_id)
        REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_privilege_rp FOREIGN KEY(privilege_id)
        REFERENCES privileges(id) ON DELETE CASCADE,
    CONSTRAINT unique_role_privilege UNIQUE(role_id, privilege_id)
);