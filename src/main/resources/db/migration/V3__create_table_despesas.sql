CREATE TABLE despesas (

    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao VARCHAR(255) NOT NULL,
    quitado boolean,
    valor_total DECIMAL(10,2),
    tipo VARCHAR(50),
    categoria_id BIGINT,
    CONSTRAINT categoria_fk FOREIGN KEY(categoria_id) REFERENCES categorias_despesas (id) ON DELETE SET NULL
);