/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers.interfaces;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.util.ControllerHelper;
import br.com.victorvilar.contaspagar.views.DespesaRecorrenteListaView;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JCheckBox;
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
public class DespesaRecorrenteListaController implements AppViewController<DespesaRecorrenteListaView>{

    private DespesaRecorrenteListaView view;
    private final DespesaService service;
    private final String TIPO_DESPESA = "RECORRENTE";
    
    @Autowired
    public DespesaRecorrenteListaController(DespesaService service){
        this.service = service;
        
    }
    
    @Override
    public void setView(DespesaRecorrenteListaView view) {
        this.view = view;
    }

    @Override
    public void limparCampos() {
        
    }
    
    public DespesaAbstrata buscarDespesa(Long id ){
        return service.findByIdWithMovimentos(id);
    }
    
    public List<DespesaAbstrata> buscarDespesas(){
        return service.findByTipo(TIPO_DESPESA);
    }
    
    public void preencherView(){
        DefaultTableModel model = (DefaultTableModel) view.getTableDespesas().getModel();
        ControllerHelper.limparTabela(model);
        List<DespesaAbstrata> despesas = buscarDespesas();
        despesas.stream().forEach(d -> { 
            DespesaRecorrente de = (DespesaRecorrente) d;
            model.addRow(new Object[]{de.getId(),de.getNomeFornecedor(),de.getAtivo()});
        });
    }

}
