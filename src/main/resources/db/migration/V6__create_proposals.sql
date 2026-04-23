CREATE TABLE proposals (
    id              BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    job_id          BIGINT          NOT NULL,
    professional_id BIGINT          NOT NULL,
    price           DECIMAL(10,2)   NOT NULL,
    estimated_time  VARCHAR(100),
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','ACCEPTED','REJECTED')),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_proposals_job          FOREIGN KEY (job_id)          REFERENCES jobs (id),
    CONSTRAINT fk_proposals_professional FOREIGN KEY (professional_id) REFERENCES profissionais (id),
    CONSTRAINT uk_proposals_job_prof     UNIQUE (job_id, professional_id)
);

CREATE INDEX idx_proposals_job_id ON proposals (job_id);
