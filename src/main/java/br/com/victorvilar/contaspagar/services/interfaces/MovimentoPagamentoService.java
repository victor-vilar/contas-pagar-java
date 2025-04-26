package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public interface MovimentoPagamentoService extends AppService<MovimentoPagamento>{
    

    public List<MovimentoPagamento> getAllNaoPagos();
    public List<MovimentoPagamento> getAllPagos();
    public List<MovimentoPagamento> update(List<MovimentoPagamento> movimentos);
    public void update();
    public List<MovimentoPagamento> getAllByDespesaId(Long id);
}
