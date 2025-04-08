package br.com.thveiculos.erp.services.despesas.interfaces;

import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;

public interface CategoriaDespesaService extends AppService<CategoriaDespesa>{

        public CategoriaDespesa getByCategoria(String categoria);
}
