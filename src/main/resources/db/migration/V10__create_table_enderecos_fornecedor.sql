create table enderecos_fornecedor(
    id BIGSERIAL PRIMARY_KEY,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(30) NOT NULL,
    bairro VARCHAR(150)NOT NULL,
    cidade VARCHAR(150) NOT NULL,
    cep VARCHAR(8),
    ponto_referencia(50),
    observacao(200)
    fornecedor_id BIGINT,
    CONSTRAINT fornecedor_fk FOREIGN_KEY(fornecedor_id)REFERENCES fornecedor(id)
);
