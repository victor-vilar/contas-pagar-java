/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.AppViewController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.util.ControllerHelper;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.MovimentoPagamentoView;
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
    private MovimentoPagamentoService movimentoService;
    
    @Autowired
    public MovimentoPagamentoController(DespesaService service, MovimentoPagamentoService movimentoService){
        this.service = service;
        this.movimentoService = movimentoService;
    }
    
    @Override
    public void setView(MovimentoPagamentoView view) {
        this.view = view;
    }


    @Override
    public void limparCampos() {
        
        view.getFieldDataInicio().setText("");
        view.getFieldDataFim().setText("");
        view.getFieldDespesa().setText("");
        view.getCheckboxPagas();
    }
    
    public void inicializarTabela(){
        preencherTabela(buscarMovimentos(true));
       
    }
    
    public List<MovimentoPagamento> buscarMovimentos(boolean pago){

        if (pago == true) {
            return movimentoService.getAllNaoPagos();
        } else {
            return movimentoService.getAllPagos();
        }

    }
    
    public void preencherTabela(List<MovimentoPagamento> movimentos) {
        
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        ControllerHelper.limparTabela(model);
        movimentos.stream().forEach(m -> {

            model.addRow(new Object[]{
                m.getId(),
                ConversorData.paraString(m.getDataVencimento()),
                m.getDespesa().getNomeFornecedor(),
                m.getReferenteParcela(),
                ConversorMoeda.paraString(m.getValorPagamento()),
                m.getFormaPagamento().getForma(),
                ConversorData.paraString(m.getDataPagamento())

            });

        });
    }
    
    public DespesaAbstrata buscarDespesa(Long id){
        return this.movimentoService.getById(id).getDespesa();
    }
    
}
