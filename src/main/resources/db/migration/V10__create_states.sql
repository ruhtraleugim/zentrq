CREATE TABLE states (
    id     BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
    name   VARCHAR(100) NOT NULL,
    uf     VARCHAR(2)   NOT NULL,
    slug   VARCHAR(100) NOT NULL,
    active BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_states_uf   UNIQUE (uf),
    CONSTRAINT uk_states_slug UNIQUE (slug)
);
