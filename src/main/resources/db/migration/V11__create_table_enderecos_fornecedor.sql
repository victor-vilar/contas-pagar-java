CREATE TABLE enderecos_fornecedor(
    id BIGSERIAL PRIMARY KEY,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(30) NOT NULL,
    bairro VARCHAR(150)NOT NULL,
    cidade VARCHAR(150) NOT NULL,
    cep VARCHAR(8),
    uf VARCHAR(2),
    ponto_referencia VARCHAR(50),
    observacao VARCHAR(200),
    fornecedor_id BIGINT,
    CONSTRAINT fornecedor_fk FOREIGN KEY(fornecedor_id)REFERENCES fornecedores(id)
);
