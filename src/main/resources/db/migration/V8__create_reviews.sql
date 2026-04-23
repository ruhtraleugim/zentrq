CREATE TABLE reviews (
    id              BIGINT    NOT NULL GENERATED ALWAYS AS IDENTITY,
    job_id          BIGINT    NOT NULL,
    client_id       BIGINT    NOT NULL,
    professional_id BIGINT    NOT NULL,
    rating          SMALLINT  NOT NULL,
    comment         TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_reviews_job          FOREIGN KEY (job_id)          REFERENCES jobs (id),
    CONSTRAINT fk_reviews_client       FOREIGN KEY (client_id)       REFERENCES clientes (id),
    CONSTRAINT fk_reviews_professional FOREIGN KEY (professional_id) REFERENCES profissionais (id),
    CONSTRAINT uk_reviews_job_id       UNIQUE (job_id)
);
