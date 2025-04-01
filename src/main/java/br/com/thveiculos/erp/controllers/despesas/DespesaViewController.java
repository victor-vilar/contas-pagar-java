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
import br.com.thveiculos.erp.enums.despesas.Periodo;
import br.com.thveiculos.erp.services.despesas.implementation.GeradorMovimentos;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.views.despesas.DespesaView;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
public class DespesaViewController implements AppViewController<DespesaView> {

    private final DespesaService service;
    private DespesaView view;
    private final CategoriaDespesaService categoriaDespesaService;
    private final FormaPagamentoService formaPagamentoService;
    private static final String DESPESA_AVULA ="AVULSA";
    private static final String DESPESA_RECORRENTE ="RECORRENTE";
    private final List<String> exludeComponents = List.of("btnNovo", "btnEditar",
            "btnSalvar", "btnDeletar", "fieldId");

   
    private Set<Integer> linhasSelecionadas;
    private List<MovimentoPagamento> movimentosSnapShot;
    
    
    
    @Autowired
    public DespesaViewController(
            DespesaService service,
            CategoriaDespesaService categoriaDespesaService,
            FormaPagamentoService formaPagamentoService) {
        this.service = service;
        this.categoriaDespesaService = categoriaDespesaService;
        this.formaPagamentoService = formaPagamentoService;
        linhasSelecionadas = new HashSet<>();
        movimentosSnapShot = new ArrayList<>();
        

    }

    @Override
    public void setView(DespesaView view) {
        this.view = view;

    }

    @Override
    public void novo() {
        enableDisableComponents(true);
        limparCampos();

    }

    @Override
    public void salvar() {

        //service.save(build());
        enableDisableComponents(false);
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
    }

    @Override
    public void limparCampos() {
        view.getTextFields().stream().forEach(f -> f.setText(""));
        view.getSpinnerQuantidadeParcelas().getModel().setValue(1);
        limparTabela();
    }

    /** 
     * Constroi o objeto que será salvo no banco de dados.
     */
    public Despesa getInstance(String type) {
       
        if(type.equals(DESPESA_AVULA)){
            return new DespesaAvulsa();
        
        }else
            return new DespesaRecorrente();
       
    }

    /**
     * Atualiza os combobox da view com os valores vindos do banco de dados.
     */
    public void atualizarCampos() {
        addValoresComboCategoria();
        addValoresComboFormaPagamento();
        addValoresComboParcelamento();

    }

    /**
     * Adiciona as categorias na combobox de categorias da view.
     */
    private void addValoresComboCategoria() {

        this.categoriaDespesaService.getTodos().stream().forEach(c -> {
            view.getComboCategoria().addItem(c.getName());
        });

        view.getComboCategoria().setSelectedIndex(-1);
    }
    

    /**
     * Adiciona as formas de pagamento nas combox da view.
     */
    private void addValoresComboFormaPagamento() {

        this.formaPagamentoService.getTodos().stream().forEach(f -> {
            view.getComboFormaPagamento().addItem(f.getName());
            view.getComboFormaPagamentoTabela().addItem(f.getName());
        });

        view.getComboFormaPagamento().setSelectedIndex(-1);
    }

    /**
     * Adiciona os valores das formas de parcelamneto na combo da view.
     */
    private void addValoresComboParcelamento() {
            
        Arrays.asList(Periodo.values()).stream().forEach(p -> 
                view.getComboParcelamento().addItem(p.name()));
        view.getComboParcelamento().setSelectedIndex(-1);
    }

    
    /**
     * Ativa ou desativa os componentes da view.
     * @param con boolean 'true' para ativar 'false' para desativar
     */
    public void enableDisableComponents(boolean con) {

        view.listaDeComponentes().stream().forEach(c -> {
            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                c.setEnabled(con);
            }
        });

    }

    /**
     * Cria os movimentos/parcelas de acordo com os valores adicionados nos campos
     * referentes as parcelas na view.
     */
    public void criarMovimentos() {
        GeradorMovimentos gm = new GeradorMovimentos();
        
        movimentosSnapShot = gm.gerarMovimentos(
                (String) view.getComboParcelamento().getSelectedItem(),
                (int)view.getSpinnerQuantidadeParcelas().getValue(),
                view.getFieldVencimentoParcela().getText(),
                view.getFieldValorTotal().getText(),
                formaPagamentoService.getByForma(String.valueOf(view.getComboFormaPagamento().getModel().getSelectedItem())));
        
        
        atualizarTabela(movimentosSnapShot);
    }
    
    
    /**
     * Limpa todos os valores que estão dentro da tabela.
     */
    public void limparTabela(){
        DefaultTableModel model = (DefaultTableModel) view.getTableParcelas().getModel();
        ControllerHelper.limparTabela(model);
    
    }

    
    /** 
     * Atualiza os valores da tabela a partir de uma lista de movimentos/parcelas
     * que forem passadas.
     * @param movimentos
     * Lista de movimentos que estão salvo ou não no banco de dados
     * que irão preencher a tabela.
     */
    public void atualizarTabela(List<MovimentoPagamento> movimentos) {
        
        DefaultTableModel model = (DefaultTableModel) view.getTableParcelas().getModel();
        ControllerHelper.limparTabela(model);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Locale localBrasil = new Locale("pt", "BR");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(localBrasil);
        
        movimentos.stream().forEach(m -> {
            model.addRow(new Object[]{m.getId(),
                m.getReferenteParcela(),
                m.getDataVencimento().format(formatter),
                formatoMoeda.format(m.getValorPagamento()),
                m.getDataPagamento(),
                m.getFormaPagamento().getName()});
        });
    }
    
    /**
     * Adiciona as linhas que foram selecionadas na tabela e que podem ter
     * sofrido alguma alteração nos seus valores, para que esses novos valores
     * alterados possam ser atualizados na lista salva em memoria.
     * @param indexLinha 
     */
    public void adicionarLinhaAlterada(int indexLinha){
        linhasSelecionadas.add(indexLinha);
    }

    
      
}
