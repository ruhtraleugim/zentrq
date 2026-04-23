CREATE TABLE profissional_skills (
    profissional_id BIGINT NOT NULL,
    skill_id        BIGINT NOT NULL,
    PRIMARY KEY (profissional_id, skill_id),
    CONSTRAINT fk_prof_skills_prof  FOREIGN KEY (profissional_id) REFERENCES profissionais (id),
    CONSTRAINT fk_prof_skills_skill FOREIGN KEY (skill_id) REFERENCES skills (id)
);
