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

    /**
     * Armazenam temporariamente os movimentos que deverão ser deletados no banco.
     * @return Lista de movimentos que devem ser deletados do banco
     */
    public List<MovimentoPagamento> getMovimentosDeletados();

    /**
     * Armazena temporariamente todos os movimentos que sofreram alteração.
     * @return Lista de movimentos que devem ser atualizados.
     */
    public List<MovimentoPagamento> getMovimentosAtualizados();

    /**
     * Adiciona movimentos que devem ser deletados da lista de movimentos.
     * @param movimento
     */
    public void addMovimentoDeletado(MovimentoPagamento movimento);

    /**
     * Adiciona um movimento que foi atualizadona lista de movimetnos atualizados.
     * @param movimento
     */
    public void addMovimentoAtualizado(MovimentoPagamento movimento);

    /**
     * Metodo para sincronizar as alterações realizadas nos movimentos como deletar e atualizar
     * com o banco de dados.
     */
    public void sincronizarMovimentos();

    /**
     * Adiciona a referencia de um movimento, ou atualiza caso algum outro movimento da lista
     * tenha sido excluido.
     * @param movimentos
     */
    public void adicionarOuAtualizarReferenteParcela(List<MovimentoPagamento> movimentos);
}
