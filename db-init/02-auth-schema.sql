\connect auth_db;

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE privileges (
    id SERIAL PRIMARY KEY,
    privilege_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE role_privileges (
    id SERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,
    CONSTRAINT fk_role_rp FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_priv FOREIGN KEY (privilege_id) REFERENCES privileges(id)
);
