package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import java.time.LocalDate;
import java.util.List;

public interface MovimentoPagamentoService extends AppService<MovimentoPagamento>{

    /**
     * Deve buscar todos os movimentos que ainda não foram pagos.
     * @return
     */
    public List<MovimentoPagamento> getAllNaoPagos();

    /**
     * Deve buscar todos os movimentos que foram pagos.
     * @return
     */
    public List<MovimentoPagamento> getAllPagos();

    /**
     * Busca os movimentos entre datas e pela nome da despesa passada.
     * @param dataInicio data mínima de busca das despesas
     * @param dataFim data máxima de busca de despesa
     * @param despesa descricao da despesa ao qual o movimento pertence
     * @param pago buscar se o movimentos esta pago ou nao
     */
    public List<MovimentoPagamento> getBetweenDatesAndDespesaName(LocalDate dataInicio, LocalDate dataFim, String despesa, boolean pago);
    
    
    /**
     * Atualiza uma lista de movimentos que foram alterados na view.
     * @param movimentos
     * @return
     */
    public List<MovimentoPagamento> update(List<MovimentoPagamento> movimentos);

    /**
     * Busca todos os movimentos que pertencem a uma despesa, procurando pelo seu id.
     * @param id
     * @return
     */
    public List<MovimentoPagamento> getAllByDespesaId(Long id);
    /**
     * salva o id da despesa temporariamente para conseguir atualizar a propriedade 'referentePagamento' dos
     * MovimentoPagamento.
     * */
    public void setIdDespesa(Long idDespesa);

    /**
     * Armazenam temporariamente os movimentos que deverão ser deletados no banco.
     * @return Lista de movimentos que devem ser deletados do banco
     */
    public List<MovimentoPagamento> getMovimentosDeletados();

    /**
     * Os serviços devem possuir uma maneira de armazenar temporariamente todos os movimentos que sofreram alteraçaõ.
     * @return Lista de movimentos que devem ser atualizados.
     */
    public List<MovimentoPagamento> getMovimentosAtualizados();

    /**
     * Adiciona movimentos que devem ser deletados da lista de movimentos.
     * @param movimento
     */
    public void addMovimentoDeletado(MovimentoPagamento movimento);

    /**
     * Adiciona um movimento que foi atualizado na lista de movimentos atualizados.
     * @param movimento
     */
    public void addMovimentoAtualizado(MovimentoPagamento movimento);

    /**
     * Metodo para sincronizar as alterações realizadas nos movimentos como deletar e atualizar, na view,
     * com o banco de dados.
     */
    public void sincronizarMovimentos();

    /**
     * Adiciona a referencia de um movimento, ou atualiza caso algum outro movimento da lista
     * tenha sido excluido.
     * @param movimentos
     */
    public void adicionarOuAtualizarReferenteParcela(List<MovimentoPagamento> movimentos);
    
    /**
     * Metodo utilizado para remover os itens que possam existir nas listas de movimento
     * service.
     */
    public void limpar();
}
