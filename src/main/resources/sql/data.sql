INSERT INTO formas_pagamento(forma) VALUES('PIX');
INSERT INTO formas_pagamento(forma) VALUES('CARTAO DE CREDITO');
INSERT INTO formas_pagamento(forma) VALUES('CARTAO DE DEBITO');
INSERT INTO formas_pagamento(forma) VALUES('BOLETO BANCÁRIO');
INSERT INTO formas_pagamento(forma) VALUES('CHEQUE');
INSERT INTO formas_pagamento(forma) VALUES('DINHEIRO');


INSERT INTO categorias_despesas(categoria) VALUES('MANUTENÇÃO VEICULAR');
INSERT INTO categorias_despesas(categoria) VALUES('ENERGIA');
INSERT INTO categorias_despesas(categoria) VALUES('DESPESAS DIVERSAS');
INSERT INTO categorias_despesas(categoria) VALUES('FOLHA DE PAGAMENTO');



INSERT INTO despesa(nome_fornecedor,descricao,categoria_id,quitado) VALUES('PAGAMENTO FOLHA SALARIAL ABRIL DE 2025','PAGAMENTO FOLHA SALARIAL ABRIL DE 2025',1,false);
INSERT INTO despesas_avulsas(id) VALUES(1);
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-2-1',100,1,1,'UNICA');


INSERT INTO despesa(nome_fornecedor,descricao,categoria_id,quitado) VALUES('PEÇAS VEICULOS UTH7645','COMPRA DE FILTRO DE OLEO \n COMPRA DE OLEO',3,false);
INSERT INTO despesas_avulsas(id) VALUES(2);
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-3-1',100,2,1,'1/5');
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-4-1',100,2,1,'2/5');
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-5-1',100,2,1,'3/5');
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-6-1',100,2,1,'4/5');
INSERT INTO movimento_pagamento(data_vencimento,valor_pagamento,despesa_id,forma_pagamento_id,referente_parcela) VALUES('2025-7-1',100,2,1,'5/5');
