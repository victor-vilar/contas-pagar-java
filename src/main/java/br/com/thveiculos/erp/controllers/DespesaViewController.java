/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers;

import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.views.despesas.DespesaView;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void salvar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void deletar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    @Override
    public void limparCampos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void atualizarTabela(){}

    
    
}
