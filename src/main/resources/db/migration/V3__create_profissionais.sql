CREATE TABLE profissionais (
    id               BIGINT       NOT NULL,
    cep              VARCHAR(9),
    rating           DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    jobs_completed   INT          NOT NULL DEFAULT 0,
    is_available_now BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_profissionais_users FOREIGN KEY (id) REFERENCES users (id)
);

CREATE INDEX idx_profissionais_available ON profissionais (is_available_now);
