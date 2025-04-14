package br.com.thveiculos.erp.services.despesas.interfaces;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public interface MovimentoPagamentoService extends AppService<MovimentoPagamento>{
    
    public List<MovimentoPagamento> getMovimentosDeletados();
    public List<MovimentoPagamento> gerarMovimentos(String parcelamento,int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento);
    public void atualizarMovimentos(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model);
    public void deletarMovimentos(List<MovimentoPagamento> movimentos, int[] linhas);
}
