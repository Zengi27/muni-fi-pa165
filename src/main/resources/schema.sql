DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(1024) NOT NULL UNIQUE,
    password VARCHAR(64),
    role VARCHAR(16) NOT NULL
);