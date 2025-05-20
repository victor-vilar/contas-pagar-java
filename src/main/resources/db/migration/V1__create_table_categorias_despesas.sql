CREATE TABLE categorias_despesas(
    id BIGSERIAL PRIMARY KEY,
    categoria VARCHAR(150) NOT NULL UNIQUE
);