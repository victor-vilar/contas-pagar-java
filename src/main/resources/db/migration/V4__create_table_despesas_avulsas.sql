CREATE TABLE despesas_avulsas (
    id BIGSERIAL PRIMARY KEY,
    CONSTRAINT id_fk FOREIGN KEY(id) REFERENCES despesas(id)

)