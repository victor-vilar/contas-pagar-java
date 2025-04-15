/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.controllers.AppViewController;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.services.despesas.interfaces.MovimentoPagamentoService;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.util.ConversorMoeda;
import br.com.thveiculos.erp.views.despesas.FinalizarMovimentoPagamentoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
@Lazy
public class FinalizarMovimentoController implements AppViewController<FinalizarMovimentoPagamentoView> {

    private FinalizarMovimentoPagamentoView view;
    private final MovimentoPagamentoService service;
    
    @Autowired
    public FinalizarMovimentoController(MovimentoPagamentoService service){
        this.service = service;
    }
    
    @Override
    public void setView(FinalizarMovimentoPagamentoView view) {
        this.view = view;
    }

    @Override
    public void novo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void salvar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void editar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deletar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
    public void buscar(Long valor){
       MovimentoPagamento pagamento = this.service.getById(valor);
       preencherCampos(pagamento);
        
    }
    
    public void preencherCampos(MovimentoPagamento pagamento) {

        view.getLabelCodigo().setText(String.valueOf(pagamento.getId()));
        view.getLabelParcela().setText(pagamento.getReferenteParcela());
        view.getLabelDescricao().setText(pagamento.getDespesa().getNomeFornecedor());
        view.getLabelDataVencimento().setText(ConversorData.paraString(pagamento.getDataVencimento()));
        view.getLabelValor().setText(ConversorMoeda.paraString(pagamento.getValorPagamento()));
        
        view.getFieldDataPagamento().setText(ConversorData.paraString(pagamento.getDataPagamento()));
        view.getFiledValorPago().setText(ConversorMoeda.paraString(pagamento.getValorPago()));
        view.getFieldObservacao().setText(pagamento.getObservacao());
        

    }

    @Override
    public void limparCampos() {
        view.getLabelCodigo().setText("-");
        view.getLabelParcela().setText("-");
        view.getLabelDescricao().setText("-");
        view.getLabelDataVencimento().setText("-");
        view.getLabelValor().setText("-");
        
        view.getFieldDataPagamento().setText("");
        view.getFiledValorPago().setText("");
        view.getFieldObservacao().setText("");

    }
    
    
}
