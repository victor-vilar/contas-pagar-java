CREATE TABLE notas_fiscais(
    id SERIAL PRIMARY KEY,
    numero VARCHAR(50) NOT NULL,
    data_emissao DATE NOT NULL,
    despesa_id BIGINT NOT NULL,
    CONSTRAINT despesa_fk FOREIGN KEY(despesa_id) REFERENCES despesas_avulsas(id)
);