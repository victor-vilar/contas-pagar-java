/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers;

import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.views.despesas.DespesaView;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author victor
 */
@Component
public class DespesaViewController implements AppViewController<DespesaView>{
    
    private final DespesaService service;
    private DespesaView view;
    private final List<String> exludeComponents = List.of("btnNovo","btnEditar","btnSalvar","btnDeletar","fieldId");
    
    @Autowired
    public DespesaViewController(DespesaService service){
        this.service =  service;
        
    }
    
    @Override
    public void setView(DespesaView view){
        this.view = view;

    }
    
    
    @Override
    public void novo() {
        enableDisableComponents(true);
        
    }
    
    @Override
    public void salvar(){
        
        //service.save(build());
        
       enableDisableComponents(false);
        
 
    }
    
    @Override
    public void deletar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    @Override
    public void limparCampos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public Despesa build(){
        return null;
    }
    
    public void atualizarTabela(){}
    
    
    public void enableDisableComponents(boolean con){
        
       view.listaDeComponentes().stream().forEach(c -> {
           if((c.getName()!= null) &&!exludeComponents.contains(c.getName())){
               c.setEnabled(con);
           }
       });

    }
    
    
    
}
