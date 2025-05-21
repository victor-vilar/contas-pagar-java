CREATE TABLE movimentos_pagamentos (

    id BIGSERIAL PRIMARY KEY,
    referente_parcela VARCHAR(100),
    data_vencimento DATE NOT NULL,
    valor_pagamento DECIMAL(10,2) NOT NULL,
    data_pagamento DATE,
    valor_pago DECIMAL(10,2),
    observacao VARCHAR(255),
    integridade VARCHAR(50) UNIQUE,
    despesa_id BIGINT,
    forma_pagamento_id BIGINT,
    CONSTRAINT despesa_fk FOREIGN KEY(despesa_id) REFERENCES despesas(id),
    CONSTRAINT forma_pagamento_fk FOREIGN KEY(forma_pagamento_id) REFERENCES formas_pagamento(id) ON DELETE SET NULL

)