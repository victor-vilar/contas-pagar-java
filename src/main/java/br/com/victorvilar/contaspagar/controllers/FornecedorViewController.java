/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.CrudViewController;
import br.com.victorvilar.contaspagar.views.FornecedorView;
import java.util.List;
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
    private final List<String> excludeComponents = List.of(
            "btnNovo",
            "btnEditar",
            "btnSalvar",
            "btnDeletar",
            "fieldId");
    
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
            if((c.getName() != null) && !excludeComponents.contains(c.getName())){;;;;
                c.setEnabled(op);                
            }
        });
           
           
    }
    
}
