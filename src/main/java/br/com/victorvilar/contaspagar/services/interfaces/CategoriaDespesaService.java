package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;

public interface CategoriaDespesaService extends AppService<CategoriaDespesa>{

        public CategoriaDespesa getByCategoria(String categoria);
}
