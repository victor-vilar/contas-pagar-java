/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
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
import javax.swing.table.DefaultTableModel;

/**
 * Controlador para os fomulários de despesas. Esses controladores precisam
 * controlar uma view que tenha extendido de DespesaView.
 * Implementações dessa classe são {@link DespesaAvulsaController} e
 * {@link DespesaRecorrenteController}.
 * @author victor
 */

public abstract class DespesaAbstractController<T extends DespesaView> implements AppViewController<T> {

    protected final DespesaService service;
    protected final MovimentoPagamentoService movimentoService;
    protected final CategoriaDespesaService categoriaDespesaService;
    protected final FormaPagamentoService formaPagamentoService;
    protected T view;
    protected List<MovimentoPagamento> movimentos;


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
            MovimentoPagamentoService movimentoService) {
        this.service = service;
        this.movimentoService = movimentoService;
        this.categoriaDespesaService = categoriaDespesaService;
        this.formaPagamentoService = formaPagamentoService;
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
     * Ativa ou desativa os componentes da view.
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
     * Atualiza o movimento de acordo com as alterações realizadas na
     * na linha da tabela na view.
     */
    public void editarMovimento(int linha) {
        movimentoService.atualizarMovimentos(
                movimentos,
                linha,
                (DefaultTableModel) view.getTableParcelas().getModel()
        );
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
    public void atualizarLinhaAlterada(int indexLinha) {
        editarMovimento(indexLinha);
        preencherTabela(movimentos);
    }

    /**
     * Remove da lista de movimentos, os movimentos que foram deletados na
     * tabela.
     *
     * @param linhas
     */
    public void deletarMovimentos(int[] linhas) {

        //utilizado o serviço para remover os movimentos que estão na tabela.
        //O serviço armazena os movimentos em uma lista para serem deletadas
        //do banco, caso já tenham sido salvas.
        movimentoService.deletarMovimentos(movimentos, linhas);

        //atualiza a view com a nova tabela.
        preencherTabela(movimentos);

    }

    /**
     * Metodo que ira disparar quando alguma valor de alguma coluna na tabela
     * alterar.
     *      *
     * @param linhas = linha que foi alterada
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
                    atualizarLinhaAlterada(linha);
                }
                break;
            //Se as coluna for igual a 3 que é a que possui valor pago
            // ira tentar ocnverter
            case 3:

                if (!String.valueOf(value).equals("")) {
                    ConversorMoeda.paraBigDecimal(String.valueOf(value));
                    atualizarLinhaAlterada(linha);
                }
                break;

            default:

                atualizarLinhaAlterada(linha);
                break;
        }
    }
    
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

    public void subscriptionCategoriaDespesa(String valor) {
        view.getComboCategoria().removeAllItems();
        inicializarComboCategoria();
        view.getComboCategoria().getModel().setSelectedItem(valor);
    }

    public void subscriptionFormaPagamento(String valor) {
        view.getComboFormaPagamento().removeAllItems();
        inicializarComboFormaPagamento();
        view.getComboFormaPagamento().getModel().setSelectedItem(valor);
    }
    
    
    public abstract void preencherView(DespesaAbstrata despesa);
}
