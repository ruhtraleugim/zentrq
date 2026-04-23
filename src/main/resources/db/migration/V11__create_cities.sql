CREATE TABLE cities (
    id       BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
    name     VARCHAR(150) NOT NULL,
    slug     VARCHAR(150) NOT NULL,
    state_id BIGINT       NOT NULL,
    active   BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_cities_state        FOREIGN KEY (state_id) REFERENCES states (id),
    CONSTRAINT uk_cities_slug_state   UNIQUE (slug, state_id)
);

CREATE INDEX idx_cities_state ON cities (state_id);
