CREATE TABLE skills (
    id     BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
    name   VARCHAR(100) NOT NULL,
    slug   VARCHAR(100) NOT NULL,
    active BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_skills_name UNIQUE (name),
    CONSTRAINT uk_skills_slug UNIQUE (slug)
);
