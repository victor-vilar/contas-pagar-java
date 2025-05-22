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
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
        pesquisar();
        
       
    }
    
    
    public void preencherTabela(List<MovimentoPagamento> movimentos) {
        
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        ControllerHelper.limparTabela(model);
        movimentos.stream().forEach(m -> {

            model.addRow(new Object[]{
                m.getId(),
                ConversorData.paraString(m.getDataVencimento()),
                m.getDespesa().getNome(),
                m.getReferenteParcela(),
                ConversorMoeda.paraString(m.getValorPagamento()),
                m.getFormaPagamento().getForma(),
                ConversorData.paraString(m.getDataPagamento())

            });

        });
    }
    
    public DespesaAbstrata buscarDespesa(Long id){
        Long idDespesa = movimentoService.getById(id).getDespesa().getId();
        return service.findByIdWithMovimentos(idDespesa);
        
    }
    
    public void pesquisar(){
    
        LocalDate dataInicial = ConversorData.paraData(view.getFieldDataInicio().getText());
        LocalDate dataFinal = ConversorData.paraData(view.getFieldDataFim().getText());
        String despesa = view.getFieldDespesa().getText();
        boolean pago = view.getCheckboxPagas().isSelected();
        
        if(dataInicial == null){
            dataInicial = LocalDate.of(2000, Month.MARCH, 1);
        }
        
        if(dataFinal == null){
            dataFinal = LocalDate.of(9999, Month.MARCH, 1);
        }
        
        List<MovimentoPagamento> movimentos = movimentoService.getBetweenDatesAndDespesaName(dataInicial, dataFinal, despesa, pago);
        preencherTabela(movimentos);
        
    }
    
    public void limparPesquisa(){
        view.getFieldDataInicio().setText("");
        view.getFieldDataFim().setText("");
        view.getFieldDataInicio().setText("");
        view.getCheckboxPagas().setSelected(false);
        pesquisar();
    }
    
    public void deletar(int[] linhas){
    
        List<Long> codigos = buscarCodigoMovimentosTabela(linhas);

        codigos.stream().forEach(c -> {
           MovimentoPagamento movimento = movimentoService.getById(c);
           movimentoService.setIdDespesa(movimento.getDespesa().getId());
           movimentoService.addMovimentoDeletado(movimento);
        });

        movimentoService.sincronizarMovimentos();
        pesquisar();
        
    }

    /**
     * Busca o id dos movimentos nas linhas selecionadas na view
     * @param linhas arrray de linhas selecionadas na tabela
     * @return lista de id dos movimentos
     */
    public List<Long> buscarCodigoMovimentosTabela(int[] linhas){
        List<Long> codigos = new ArrayList<>();
        DefaultTableModel model = getTabelaModel();
        Long codigo;
        for(int i = 0; i< linhas.length; i++){
            codigo = (Long) model.getValueAt(linhas[i], 0);
            codigos.add(codigo);
        }
        return codigos;
    }

    /**
     * Busca o table model da view
     * @return table model da view
     */
    public DefaultTableModel getTabelaModel(){
        return (DefaultTableModel) view.getTableMovimentos().getModel();
    }
}
