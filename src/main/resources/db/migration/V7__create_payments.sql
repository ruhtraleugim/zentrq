CREATE TABLE payments (
    id           BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
    job_id       BIGINT        NOT NULL,
    proposal_id  BIGINT        NOT NULL,
    amount       DECIMAL(10,2) NOT NULL,
    platform_fee DECIMAL(10,2) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','PAID','FAILED')),
    external_id  VARCHAR(255),
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_payments_job        FOREIGN KEY (job_id)      REFERENCES jobs (id),
    CONSTRAINT fk_payments_proposal   FOREIGN KEY (proposal_id) REFERENCES proposals (id),
    CONSTRAINT uk_payments_job_id     UNIQUE (job_id),
    CONSTRAINT uk_payments_proposal_id UNIQUE (proposal_id)
);
