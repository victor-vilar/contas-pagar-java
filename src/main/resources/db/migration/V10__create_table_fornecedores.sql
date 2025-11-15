CREATE TABLE fornecedores (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(300),
    nome_fantasia VARCHAR(150),
    cpf_cnpj VARCHAR(14) UNIQUE NOT NULL,
    observacao VARCHAR(200)
    
)
