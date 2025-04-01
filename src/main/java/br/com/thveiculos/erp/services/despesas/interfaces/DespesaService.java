package br.com.thveiculos.erp.services.despesas.interfaces;

import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public interface DespesaService extends AppService<Despesa>{

       public List<MovimentoPagamento> gerarMovimentos(String parcelamento,int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento);
       public void atualizarMovimentos(List<MovimentoPagamento> movimentos, Set<Integer> linhas, DefaultTableModel model);
}
