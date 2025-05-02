/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.CrudViewController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.util.ControllerHelper;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.views.interfaces.DespesaView;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador para os fomulários de despesas. Esses controladores precisam
 * controlar uma view que tenha extendido de DespesaView.
 * Implementações dessa classe são {@link DespesaAvulsaController} e
 * {@link DespesaRecorrenteController}.
 * @author victor
 */

public abstract class DespesaAbstractController<T extends DespesaView> implements CrudViewController<T> {

    protected final DespesaService service;
    protected final MovimentoPagamentoService movimentoService;
    protected final DespesaControllerHelper controllerHelper;
    protected final CategoriaDespesaService categoriaDespesaService;
    protected final FormaPagamentoService formaPagamentoService;
    protected T view;
    protected List<MovimentoPagamento> movimentos;
    public static final String REMOVER_TODAS_PARCELAS =
            "Não é possível remover todas as parcelas de uma despesa.\n" +
                    "A despesa precisa possuir pelo menos uma parcela.";


    private final List<String> exludeComponents = List.of(
            "btnNovo",
            "btnEditar",
            "btnSalvar",
            "btnDeletar",
            "fieldId");

    

   
    public DespesaAbstractController(
            DespesaService service,
            CategoriaDespesaService categoriaDespesaService,
            FormaPagamentoService formaPagamentoService,
            MovimentoPagamentoService movimentoService,
            DespesaControllerHelper controllerHelper) {
        this.service = service;
        this.movimentoService = movimentoService;
        this.categoriaDespesaService = categoriaDespesaService;
        this.formaPagamentoService = formaPagamentoService;
        this.controllerHelper = controllerHelper;
        movimentos = new ArrayList<>();

    }

    public List<MovimentoPagamento> getMovimentos(){
        return movimentos;
    }
    
    public void setMovimentos(List<MovimentoPagamento> movimentos){
        this.movimentos = movimentos;
    }

    public abstract void checarErrosAoSalvar();
    
    
    @Override
    public void setView(T view){
        this.view = view;
    }
            
    
    @Override
    public void novo() {        
        enableDisableComponents(true);
        limparCampos();
        

    }

    @Override
    public void editar() {
        if (view.getFieldId().getText().equals("")) {
            return;
        }

        enableDisableComponents(true);
    }
    
    @Override
    public void deletar() {
        service.deleteById(Long.valueOf(view.getFieldId().getText()));
        limparCampos();
    }
    
    @Override
    public void limparCampos() {
        view.getTextFields().stream().forEach(f -> f.setText(""));
        view.getComboBoxes().stream().forEach(c -> c.setSelectedIndex(-1));
        limparTabela();
        movimentos.clear();
        movimentoService.limpar();
        ativarDesativarTabelaParcelas(false);
    }


    /**
     * Atualiza os combobox da view com os valores vindos do banco de dados.
     */
    public void inicializarComboBox() {
        resetarCombos();
        inicializarComboCategoria();
        inicializarComboFormaPagamento();
        inicializarComboParcelamento();

    }

    public void resetarCombos() {
        view.getComboBoxes().stream().forEach(c -> c.removeAllItems());
    }

    /**
     * Adiciona as categorias na combobox de categorias da view.
     */
    public void inicializarComboCategoria() {

        this.categoriaDespesaService.getTodos().stream().forEach(c -> {
            view.getComboCategoria().addItem(c.getName());
        });

        view.getComboCategoria().setSelectedIndex(-1);
    }

    /**
     * Adiciona as formas de pagamento nas combox da view.
     */
    public void inicializarComboFormaPagamento() {

        this.formaPagamentoService.getTodos().stream().forEach(f -> {
            view.getComboFormaPagamento().addItem(f.getName());
            view.getComboFormaPagamentoTabela().addItem(f.getName());
        });

        view.getComboFormaPagamento().setSelectedIndex(-1);
    }

    /**
     * Adiciona os valores das formas de parcelamneto na combo da view.
     */
    public void inicializarComboParcelamento() {

        Arrays.asList(Periodo.values()).stream().forEach(p
                -> view.getComboParcelamento().addItem(p.name()));
        view.getComboParcelamento().setSelectedIndex(-1);
    }

    /**
     * Ativa ou desativa os componentes da view. Ignorando os que se encontram
     * dentro da lista 'excludedComponents', já que esses valores devem sempre
     * estar ativos.
     *
     * @param con boolean 'true' para ativar 'false' para desativar
     */
    public void enableDisableComponents(boolean con) {

        view.getAllComponentes().stream().forEach(c -> {
            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                c.setEnabled(con);
            }
        });

    }



    /**
     * Atualiza os valores da tabela a partir de uma lista de
     * movimentos/parcelas que forem passadas.
     *
     * @param movimentos Lista de movimentos que estão salvo ou não no banco de
     * dados que irão preencher a tabela.
     */
    public void preencherTabela(List<MovimentoPagamento> movimentos) {

        DefaultTableModel model = (DefaultTableModel) view.getTableParcelas().getModel();
        ControllerHelper.limparTabela(model);

        movimentos.stream().forEach(m -> {
            model.addRow(new Object[]{m.getId(),
                m.getReferenteParcela(),
                ConversorData.paraString(m.getDataVencimento()),
                ConversorMoeda.paraString(m.getValorPagamento()),
                ConversorData.paraString(m.getDataPagamento()),
                m.getFormaPagamento().getName(),
                m.getObservacao()});
        });

    }

    /**
     * Limpa todos os valores que estão dentro da tabela.
     */
    protected void limparTabela() {
        DefaultTableModel model = (DefaultTableModel) view.getTableParcelas().getModel();
        ControllerHelper.limparTabela(model);

    }



    /**
     * Adiciona as linhas que foram selecionadas na tabela e que podem ter
     * sofrido alguma alteração nos seus valores, para que esses novos valores
     * alterados possam ser atualizados na lista salva em memoria.
     *
     * @param indexLinha
     */
    public void atualizarMovimento(int indexLinha) {
        editarMovimento(indexLinha);
        preencherTabela(movimentos);
    }

    /**
     * Atualiza o movimento de acordo com as alterações realizadas na
     * na linha da tabela na view.
     * Adiciona o movimento atualizado a lista de movimentos atualizados de movimentoPagamentoService
     * para que os dados possam ser salvos no banco de dados após o salvamento.
     * @param linha linha na tabela da view que foi alterada
     */
    public void editarMovimento(int linha) {
        movimentoService.addMovimentoAtualizado(controllerHelper.atualizarMovimentosTabela(
                movimentos,
                linha,
                (DefaultTableModel) view.getTableParcelas().getModel()
        ));

        movimentoService.adicionarOuAtualizarReferenteParcela(this.movimentos);
    }

    /**
     * Remove da lista de movimentos, os movimentos que foram deletados na
     * tabela.
     *
     * @param linhas linhas selecionadas na view
     */
    public void deletarMovimentos(int[] linhas) {

        int totalDeLinhas = view.getTableParcelas().getRowCount();
        Long cod = (Long) view.getTableParcelas().getValueAt(0,0);

        //Aqui so sera permitido remover TODOS os itens se eles ainda nao tiverem sido salvos no banco
        if(totalDeLinhas == linhas.length && cod != null){
            throw new QuantidadeDeParcelasException(REMOVER_TODAS_PARCELAS);
        }

        controllerHelper.deletarMovimentosTabela(movimentos, linhas)
                .stream()
                .forEach(e -> movimentoService.addMovimentoDeletado(e));

        //atualiza a view com a nova tabela.
        movimentoService.adicionarOuAtualizarReferenteParcela(this.movimentos);
        preencherTabela(movimentos);

    }

    /**
     * Metodo que ira disparar quando alguma valor de alguma coluna na tabela
     * alterar.
     *      *
     * @param linha = linha que foi alterada
     * @param coluna = coluna que foi alterada
     * @param value = valor que sera usado para realizar a conversão
     */
    public void eventoTableChanged(int linha, int coluna, Object value) throws DateTimeParseException {
        switch (coluna) {
            //Se as colunas forem ou 2 ou 4 que são as que possuem datas,
            //ira tentar converter o valor em data;
            case 2:
            case 4:

                if (!String.valueOf(value).equals("")) {
                    ConversorData.paraData(String.valueOf(value));
                    atualizarMovimento(linha);
                }
                break;
            //Se as coluna for igual a 3 que é a que possui valor pago
            // ira tentar ocnverter
            case 3:

                if (!String.valueOf(value).equals("")) {
                    ConversorMoeda.paraBigDecimal(String.valueOf(value));
                    atualizarMovimento(linha);
                }
                break;

            default:

                atualizarMovimento(linha);
                break;
        }
    }
    
    /**
     * Atualiza o valor da combobox de acordo com o que foi
     * selecionado em seus respectivos formulários de cadastro. 
     * @param valor = Valor que deve ser colocado na combobox.
     * @param tipo = Nome do formulario que representa o objeto que será manipulado
     */
    public void aoSusbscrever(String valor, String tipo) {

        switch (tipo) {
            case "Categoria Despesas":
                subscriptionCategoriaDespesa(valor);
                break;
            case "Formas Pagamento":
                subscriptionFormaPagamento(valor);
                break;
        }
    }

    /**
     * Recebe o valor vindo do formulário {@link CategoriaDespesaView}, então
     * limpa a combobox, busca novos valores adicionados no banco de dados, e
     * seleciona o mesmo valor na combobox que foi passado pelo formulário.
     * 
     * @param valor = Nome do CategoriaDespesa selecionada
     */
    public void subscriptionCategoriaDespesa(String valor) {
        view.getComboCategoria().removeAllItems();
        inicializarComboCategoria();
        view.getComboCategoria().getModel().setSelectedItem(valor);
    }

        /**
     * Recebe o valor vindo do formulário {@link FormaPagamentoView}, então
     * limpa a combobox, busca novos valores adicionados no banco de dados, e
     * seleciona o mesmo valor na combobox que foi passado pelo formulário.
     * 
     * @param valor = Nome da FormaPagamento selecionada
     */
    public void subscriptionFormaPagamento(String valor) {
        view.getComboFormaPagamento().removeAllItems();
        inicializarComboFormaPagamento();
        view.getComboFormaPagamento().getModel().setSelectedItem(valor);
    }
    
    /**
     * Metodo utilizado para ativar ou desativar a tabela de parcelas na view.
     * @param ativar true para ativar , 'false' para desativar
     */
    public void ativarDesativarTabelaParcelas(boolean ativar){
        
        view.getTableParcelas().setEnabled(ativar);
        
        if(ativar){
            view.getBtnLockTable().setIcon(new ImageIcon(getClass().getResource("/img/icon-unlock.png")));
        }else{
            view.getBtnLockTable().setIcon(new ImageIcon(getClass().getResource("/img/icon-lock.png")));
        }
        
    }
    
    /** 
     * Preenche os campos da view com os valores das despeas passadas, cada
     * objeto e subclasse de DespesaAbstractController possuem diferentes 
     * quantidades de campos, então suas subclasses devem implementar esse metodo.
     * No formulário {@link br.com.victorvilar.contaspagar.views.MovimentoPagamentoView}
     * ao selecionar um movimento na tabela e apertar F1, o sistema busca no banco de dados
     * a despesa ao qual o movimento selecionado pertence e abre o seu respectivo formulário
     * dependendo do tipo de despesa.
     */
    public abstract void preencherView(DespesaAbstrata despesa);
}
