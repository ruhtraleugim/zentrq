CREATE TABLE profissional_cities (
    profissional_id BIGINT       NOT NULL,
    city            VARCHAR(100) NOT NULL,
    CONSTRAINT fk_prof_cities_profissional FOREIGN KEY (profissional_id) REFERENCES profissionais (id)
);
