CREATE TABLE despesas_recorrentes (
    id BIGINT PRIMARY KEY,
    periocidade VARCHAR(50),
    ativo boolean,
    data_proximo_lancamento DATE,
    data_ultimo_lancamento DATE,
    dia_pagamento INT NOT NULL,
    mes_pagamento INT,
    forma_pagamento_padrao BIGINT,
    CONSTRAINT id_fk FOREIGN KEY(id) REFERENCES despesas (id),
    CONSTRAINT forma_pagamento_fk FOREIGN KEY(forma_pagamento_padrao) REFERENCES formas_pagamento(id) ON DELETE SET NULL

)