/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.CrudViewController;
import br.com.victorvilar.contaspagar.entities.EnderecoFornecedor;
import br.com.victorvilar.contaspagar.entities.Fornecedor;
import br.com.victorvilar.contaspagar.enums.UF;
import br.com.victorvilar.contaspagar.services.interfaces.FornecedorService;
import br.com.victorvilar.contaspagar.views.FornecedorView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
@Lazy
public class FornecedorViewController implements CrudViewController<FornecedorView>  {

    
    private FornecedorView view;
    private final FornecedorService service;
    private final List<String> excludeComponents = List.of(
            "btnNovo",
            "btnEditar",
            "btnSalvar",
            "btnDeletar",
            "fieldId");
    
    @Autowired
    public FornecedorViewController(FornecedorService service){
        this.service = service;
    }
    
    
    @Override
    public void novo() {
        ativarOuDesativarCampos(true);
        limparCampos();
    }

    @Override
    public void salvar() {
        ativarOuDesativarCampos(false);
    }

    @Override
    public void editar() {
        ativarOuDesativarCampos(false);
    }

    @Override
    public void deletar() {
        this.service.deleteById(Long.valueOf(view.getFieldId().getText()));
        ativarOuDesativarCampos(false);
    }

    @Override
    public void setView(FornecedorView view) {
       this.view = view;
    }

    @Override
    public void limparCampos() {
     view.getAllTextFields().stream().forEach(f -> f.setText(""));
     view.getAllComboBoxes().stream().forEach(c -> c.setSelectedIndex(-1));
    }
    
    public void ativarOuDesativarCampos(boolean op){
        view.getAllComponents().stream().forEach(c ->{
            if((c.getName() != null) && !excludeComponents.contains(c.getName())){
                c.setEnabled(op);                
            }
        });
           
           
    }
    
    public Fornecedor criarFornecedor(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(view.getFieldNome().getText().trim());
        fornecedor.setCpfCnpj(view.getFieldCpfCnpj().getText().trim());
        fornecedor.setNomeFantasia(view.getFieldFantasia().getText().trim());
        fornecedor.setObservacao(view.getFieldObservacao().getText().trim());
        fornecedor.setEndereco(criarEndereco());
        return fornecedor;
    }
    
    public EnderecoFornecedor criarEndereco(){
        EnderecoFornecedor endereco = new EnderecoFornecedor();
        endereco.setLogradouro(view.getFieldLogradouro().getText().trim());
        endereco.setNumero(view.getFieldNumero().getText().trim());
        endereco.setBairro(view.getFieldBairro().getText().trim());
        endereco.setCidade(view.getFieldCidade().getText().trim());
        endereco.setCep(view.getFieldCep().getText().trim());
        endereco.setUf(UF.fromSigla((String) view.getComboUF().getSelectedItem()));
        return endereco;
    }
    
}
