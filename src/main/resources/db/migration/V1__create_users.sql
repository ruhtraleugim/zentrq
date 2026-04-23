CREATE TABLE users (
    id              BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(150)    NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    dtype           VARCHAR(30)     NOT NULL,
    role            VARCHAR(20)     NOT NULL,
    email_verified  BOOLEAN         NOT NULL DEFAULT FALSE,
    otp_code        VARCHAR(6),
    otp_expires_at  TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);
