DROP INDEX idx_jobs_city_status;
ALTER TABLE jobs DROP COLUMN city;
ALTER TABLE jobs ADD COLUMN city_id BIGINT NOT NULL;
ALTER TABLE jobs ADD CONSTRAINT fk_jobs_city FOREIGN KEY (city_id) REFERENCES cities (id);
CREATE INDEX idx_jobs_city_status ON jobs (city_id, status);
