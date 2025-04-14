/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers;

import br.com.thveiculos.erp.controllers.util.ControllerHelper;
import br.com.thveiculos.erp.entities.despesas.DespesaAbstrata;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.views.despesas.MovimentoPagamentoView;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
public class MovimentoPagamentoController implements AppViewController<MovimentoPagamentoView> {
    
    private MovimentoPagamentoView view;
    private DespesaService service;
    
    @Autowired
    public MovimentoPagamentoController(DespesaService service){
        this.service = service;
    }
    
    @Override
    public void setView(MovimentoPagamentoView view) {
        this.view = view;
    }

    @Override
    public void novo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void salvar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void editar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void limparCampos() {
        
        view.getFieldDataInicio().setText("");
        view.getFieldDataFim().setText("");
        view.getFieldDespesa().setText("");
        view.getCheckboxPagas();
    }
    
    public void inicializarTabela(){
    
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        ControllerHelper.limparTabela(model);
        
        List<DespesaAbstrata> despesa = service.getTodos();

        
    }
    
}
