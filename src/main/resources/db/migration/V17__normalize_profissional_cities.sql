DROP TABLE IF EXISTS profissional_cities;

CREATE TABLE profissional_cities (
    profissional_id BIGINT NOT NULL,
    city_id         BIGINT NOT NULL,
    PRIMARY KEY (profissional_id, city_id),
    CONSTRAINT fk_prof_cities_prof FOREIGN KEY (profissional_id) REFERENCES profissionais (id),
    CONSTRAINT fk_prof_cities_city FOREIGN KEY (city_id) REFERENCES cities (id)
);
