/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.EnderecoFornecedor;
import br.com.victorvilar.contaspagar.entities.Fornecedor;
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
        view.getFieldBairro().setText("teste");
        view.getFieldCep().setText("teste");
        view.getFieldCidade().setText("teste");
        view.getFieldLogradouro().setText("teste");
        view.getFieldNumero().setText("teste");
        view.getComboUF().setSelectedIndex(1);
        EnderecoFornecedor endereco = controller.criarEndereco();
        assertEquals(endereco.getBairro(), view.getFieldBairro().getText());
        assertEquals(endereco.getCidade(), view.getFieldBairro().getText());
        assertEquals(endereco.getCep(), view.getFieldBairro().getText());
        assertEquals(endereco.getLogradouro(), view.getFieldLogradouro().getText());
        assertEquals(endereco.getNumero(), view.getFieldNumero().getText());
        assertEquals(endereco.getUf().toString(),(String) view.getComboUF().getSelectedItem());
    }
    
    @Test
    public void metodoDeveCriarUmNovoFornecedorComInformacoesDaView(){
        view.getFieldBairro().setText("teste");
        view.getFieldCep().setText("teste");
        view.getFieldCidade().setText("teste");
        view.getFieldCpfCnpj().setText("teste");
        view.getFieldFantasia().setText("teste");
        view.getFieldId().setText("teste");
        view.getFieldLogradouro().setText("teste");
        view.getFieldNome().setText("teste");
        view.getFieldNumero().setText("teste");
        view.getFieldObservacao().setText("teste");
        view.getComboUF().setSelectedIndex(1);
        
        Fornecedor fornecedor = controller.criarFornecedor();
        assertEquals(fornecedor.getCpfCnpj(), view.getFieldCpfCnpj().getText());
        assertEquals(fornecedor.getRazaoSocial(), view.getFieldNome().getText());
        assertEquals(fornecedor.getNomeFantasia(), view.getFieldFantasia().getText());
        assertEquals(fornecedor.getObservacao(), view.getFieldObservacao().getText());
        assertEquals(fornecedor.getEndereco().getBairro(), view.getFieldBairro().getText());
        assertEquals(fornecedor.getEndereco().getCidade(), view.getFieldBairro().getText());
        assertEquals(fornecedor.getEndereco().getCep(), view.getFieldBairro().getText());
        assertEquals(fornecedor.getEndereco().getLogradouro(), view.getFieldLogradouro().getText());
        assertEquals(fornecedor.getEndereco().getNumero(), view.getFieldNumero().getText());
        assertEquals(fornecedor.getEndereco().getUf().toString(),(String) view.getComboUF().getSelectedItem());
    }
    
    @Test
    public void metodosDeveLimparTodosOsCamposDaView(){
        view.getFieldBairro().setText("teste");
        view.getFieldCep().setText("teste");
        view.getFieldCidade().setText("teste");
        view.getFieldCpfCnpj().setText("teste");
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
        assertEquals(view.getFieldCpfCnpj().getText(),"");
        assertEquals(view.getFieldFantasia().getText(),"");
        assertEquals(view.getFieldId().getText(),"");
        assertEquals(view.getFieldLogradouro().getText(),"");
        assertEquals(view.getFieldNome().getText(),"");
        assertEquals(view.getFieldNumero().getText(),"");
        assertEquals(view.getFieldObservacao().getText(),"");
        assertEquals(view.getComboUF().getSelectedIndex(),-1);
                
    }
    
}
