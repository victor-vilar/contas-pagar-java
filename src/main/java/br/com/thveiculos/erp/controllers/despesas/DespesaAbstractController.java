/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.controllers.AppViewController;
import br.com.thveiculos.erp.controllers.util.ControllerHelper;
import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.entities.despesas.DespesaAvulsa;
import br.com.thveiculos.erp.entities.despesas.DespesaRecorrente;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.entities.despesas.NotaFiscal;
import br.com.thveiculos.erp.enums.despesas.Periodo;
import br.com.thveiculos.erp.util.ConversorMoeda;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.views.despesas.DespesaAvulsaViewImpl;
import br.com.thveiculos.erp.views.interfaces.DespesaView;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author victor
 */

public abstract class DespesaAbstractController<T extends DespesaView> implements AppViewController<T> {

    protected final DespesaService service;
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
            FormaPagamentoService formaPagamentoService) {
        this.service = service;
        this.categoriaDespesaService = categoriaDespesaService;
        this.formaPagamentoService = formaPagamentoService;
        movimentos = new ArrayList<>();

    }


    
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
        limparCamposParcelamento();
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

    private void resetarCombos() {
        view.getComboBoxes().stream().forEach(c -> c.removeAllItems());
    }

    /**
     * Adiciona as categorias na combobox de categorias da view.
     */
    void inicializarComboCategoria() {

        this.categoriaDespesaService.getTodos().stream().forEach(c -> {
            view.getComboCategoria().addItem(c.getName());
        });

        view.getComboCategoria().setSelectedIndex(-1);
    }

    /**
     * Adiciona as formas de pagamento nas combox da view.
     */
    private void inicializarComboFormaPagamento() {

        this.formaPagamentoService.getTodos().stream().forEach(f -> {
            view.getComboFormaPagamento().addItem(f.getName());
            view.getComboFormaPagamentoTabela().addItem(f.getName());
        });

        view.getComboFormaPagamento().setSelectedIndex(-1);
    }

    /**
     * Adiciona os valores das formas de parcelamneto na combo da view.
     */
    private void inicializarComboParcelamento() {

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
     * Atualiza a lista de movimentos de acordo com as alterações realizadas na
     * tabela da view.
     */
    protected void atualizarMovimento(int linha) {
        service.atualizarMovimentos(
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
    protected void preencherTabela(List<MovimentoPagamento> movimentos) {

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
     * Limpar somente os campos referente as parcelas no formulário.
     */
    protected void limparCamposParcelamento() {
        view.getComboParcelamento().setSelectedIndex(-1);
        view.getFieldValor().setText("");
        view.getComboFormaPagamento().setSelectedIndex(-1);
    }

    /**
     * Adiciona as linhas que foram selecionadas na tabela e que podem ter
     * sofrido alguma alteração nos seus valores, para que esses novos valores
     * alterados possam ser atualizados na lista salva em memoria.
     *
     * @param indexLinha
     */
    public void atualizarLinhaAlterada(int indexLinha) {
        atualizarMovimento(indexLinha);
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
        service.deletarMovimentos(movimentos, linhas);

        //atualiza a view com a nova tabela.
        preencherTabela(movimentos);

    }

    /**
     * Metodo que ira disparar quando alguma valor de alguma coluna na tabela
     * alterar.
     *
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
}
