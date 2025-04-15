package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public interface MovimentoPagamentoService extends AppService<MovimentoPagamento>{
    
    public List<MovimentoPagamento> getMovimentosDeletados();
    public List<MovimentoPagamento> gerarMovimentos(String parcelamento,int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento);
    public void atualizarMovimentos(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model);
    public void deletarMovimentos(List<MovimentoPagamento> movimentos, int[] linhas);
    public List<MovimentoPagamento> getAllNaoPagos();
    public List<MovimentoPagamento> getAllPagos();
}
