CREATE TABLE despesas (

    id BIGINT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao VARCHAR(255) NOT NULL,
    quitado boolean,
    valor_total DECIMAL(10,2),
    tipo VARCHAR(50),
    categoria_fk BIGINT REFERENCES categorias_despesas (id)


);