/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.controllers.AppViewController;
import br.com.thveiculos.erp.controllers.util.ControllerHelper;
import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.entities.despesas.DespesaAvulsa;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.views.despesas.DespesaView;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final List<String> exludeComponents = List.of("btnNovo", "btnEditar",
            "btnSalvar", "btnDeletar", "fieldId");

    @Autowired
    public DespesaViewController(
            DespesaService service,
            CategoriaDespesaService categoriaDespesaService,
            FormaPagamentoService formaPagamentoService) {
        this.service = service;
        this.categoriaDespesaService = categoriaDespesaService;
        this.formaPagamentoService = formaPagamentoService;

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
        if(view.getFieldId().getText().equals("")){
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
        view.getSpinnerQuantidadeParcelas().getModel().setValue(0);
    }

    public Despesa build() {
        DespesaAvulsa despesa = new DespesaAvulsa();
        
        
        return null;
    }

    public void atualizarCampos() {
        addValoresComboCategoria();
        addValoresComboFormaPagamento();
        addValoresParcelamento();

    }

    public void addValoresComboCategoria() {
    
        this.categoriaDespesaService.getTodos().stream().forEach(c ->{ 
            System.out.println(c.getName());
            view.getComboCategoria().addItem(c.getName());
        });
        
        view.getComboCategoria().setSelectedIndex(-1);
    }

    public void addValoresComboFormaPagamento() {
        
        this.formaPagamentoService.getTodos().stream().forEach(f ->{ 
            System.out.println(f.getName());
            view.getComboFormaPagamento().addItem(f.getName());
        });
        
        view.getComboFormaPagamento().setSelectedIndex(-1);
    }

    public void addValoresParcelamento() {

    }

    public void enableDisableComponents(boolean con) {

        view.listaDeComponentes().stream().forEach(c -> {
            if ((c.getName() != null) && !exludeComponents.contains(c.getName())) {
                c.setEnabled(con);
            }
        });

    }
    
    public void atualizarTabela(List<MovimentoPagamento> movimentos){
        DefaultTableModel model = (DefaultTableModel) view.getTableParcelas().getModel();
        ControllerHelper.limparTabela(model);
        movimentos.stream().forEach(m ->{
            model.addRow(new Object[]{m.getId(),m.getReferenteParcela(),m.getDataVencimento(),m.getValorPagamento(),m.getDataPagamento()});
        });
    }

}
