CREATE TABLE clientes (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_clientes_users FOREIGN KEY (id) REFERENCES users (id)
);
