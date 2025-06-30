package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.DespesaTipo;

import java.util.List;
import javax.swing.table.DefaultTableModel;

public interface DespesaService extends AppService<DespesaAbstrata>{
    
    public DespesaAbstrata findByIdWithMovimentos(Long id);
    public List<DespesaAbstrata> findByTipo(DespesaTipo tipo);

}
