CREATE TABLE despesas_avulsas (
    id BIGINT NOT NULL
    CONSTRAINT id_fk FOREIGN KEY(id) REFERENCES despesas_avulsas(id)

)