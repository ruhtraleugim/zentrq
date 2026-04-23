CREATE TABLE jobs (
    id          BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
    client_id   BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    type        VARCHAR(20)  NOT NULL CHECK (type IN ('NORMAL','URGENT')),
    status      VARCHAR(30)  NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN','IN_NEGOTIATION','ACCEPTED','IN_PROGRESS','COMPLETED','CANCELED')),
    city        VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_jobs_client FOREIGN KEY (client_id) REFERENCES clientes (id)
);

CREATE INDEX idx_jobs_city_status ON jobs (city, status);
