/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.views.FornecedorView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class FornecedorViewControllerTest {


    @InjectMocks
    @Spy
    private FornecedorViewController controller;
    private FornecedorView view;
    
    @BeforeEach
    public void setUp(){
        this.view = new FornecedorView(controller);
        view.inicializarFormulario();
    }
   
    @Test
    public void metodoDeveAtivarTodosOsCamposDaViewQueNaoEstaoNaLista(){
        
    }
    
    @Test void metodoDeveDesativarTodosOsCamposDaView(){
    }
    
    @Test
    public void metodoDeveCriarUmNovoEnderecoComInformacoesDaView(){
       
    }
    
    @Test
    public void metodoDeveCriarUmNovoFornecedorComInformacoesDaView(){
    }
    
    @Test
    public void metodosDeveLimparTodosOsCamposDaView(){
        view.getFieldBairro().setText("teste");
        view.getFieldCep().setText("teste");
        view.getFieldCidade().setText("teste");
        view.getFieldCnpjCpf().setText("teste");
        view.getFieldFantasia().setText("teste");
        view.getFieldId().setText("teste");
        view.getFieldLogradouro().setText("teste");
        view.getFieldNome().setText("teste");
        view.getFieldNumero().setText("teste");
        view.getFieldObservacao().setText("teste");
        view.getComboUF().setSelectedIndex(1);
        controller.limparCampos();
        
        assertEquals(view.getFieldBairro().getText(),"");
        assertEquals(view.getFieldCep().getText(),"");
        assertEquals(view.getFieldCidade().getText(),"");
        assertEquals(view.getFieldCnpjCpf().getText(),"");
        assertEquals(view.getFieldFantasia().getText(),"");
        assertEquals(view.getFieldId().getText(),"");
        assertEquals(view.getFieldLogradouro().getText(),"");
        assertEquals(view.getFieldNome().getText(),"");
        assertEquals(view.getFieldNumero().getText(),"");
        assertEquals(view.getFieldObservacao().getText(),"");
        assertEquals(view.getComboUF().getSelectedIndex(),-1);
                
    }
    
}
