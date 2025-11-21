/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.AppViewController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.Fornecedor;
import br.com.victorvilar.contaspagar.services.interfaces.FornecedorService;
import br.com.victorvilar.contaspagar.util.ControllerHelper;
import br.com.victorvilar.contaspagar.views.FornecedorListaView;
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
@Lazy
public class FornecedorListaViewController implements AppViewController<FornecedorListaView> {

    private FornecedorListaView view;
    private FornecedorService service;
    
    @Autowired
    public FornecedorListaViewController(FornecedorService service){
        this.service= service;
    }
    
    @Override
    public void setView(FornecedorListaView view) {
        this.view =view;
    }

    @Override
    public void limparCampos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public void preencherView(){
        DefaultTableModel model = (DefaultTableModel) view.getTableFornecedores().getModel();
        ControllerHelper.limparTabela(model);
        List<Fornecedor> fornecedores = service.getTodos();
        fornecedores.stream().forEach(f -> {
            model.addRow(new Object[]{f.getId(),f.getRazaoSocial(),f.getNomeFantasia(),f.getCpfCnpj()});
        });
    }
    
    public Fornecedor buscarFornecedor(Long id){
        return this.service.getById(id);
    }
    
}
