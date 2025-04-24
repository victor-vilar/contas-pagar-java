/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.controllers.interfaces.AppViewController;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.FinalizarMovimentoPagamentoView;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private MovimentoPagamento movimento;
    private FormaPagamentoService formaPagamentoService;
    
    @Autowired
    public FinalizarMovimentoController(MovimentoPagamentoService service, FormaPagamentoService formaPagamentoService){
        this.service = service;
        this.formaPagamentoService = formaPagamentoService;
    }
    
    @Override
    public void setView(FinalizarMovimentoPagamentoView view) {
        this.view = view;
    }

    public void salvar() {
        
        LocalDate dataPagamento = ConversorData.paraData(view.getFieldDataPagamento().getText());
        BigDecimal valorPago = ConversorMoeda.paraBigDecimal(view.getFieldValorPago().getText());
        FormaPagamento forma = formaPagamentoService.getByForma(view.getComboFormaPagamento().getSelectedItem().toString());
        String observacao = view.getFieldObservacao().getText();
        
        movimento.setDataPagamento(dataPagamento);
        movimento.setValorPago(valorPago);
        movimento.setFormaPagamento(forma);
        movimento.setObservacao(observacao);
        this.service.save(movimento);
    }

 
    public void buscar(Long valor){
       movimento = this.service.getById(valor);
       preencherCampos(movimento);
        
    }
    
    public void preencherCampos(MovimentoPagamento pagamento) {

        view.getFieldCodigo().setText(String.valueOf(pagamento.getId()));
        view.getFieldParcela().setText(pagamento.getReferenteParcela());
        view.getFieldDescricao().setText(pagamento.getDespesa().getNomeFornecedor());
        view.getFieldVencimento().setText(ConversorData.paraString(pagamento.getDataVencimento()));
        view.getFieldValor().setText(ConversorMoeda.paraString(pagamento.getValorPagamento()));
        
        view.getFieldDataPagamento().setText(ConversorData.paraString(pagamento.getDataPagamento()));
        view.getFieldValorPago().setText(ConversorMoeda.paraString(pagamento.getValorPago()));
        view.getComboFormaPagamento().getModel().setSelectedItem(pagamento.getFormaPagamento().getForma());
        view.getFieldObservacao().setText(pagamento.getObservacao());
        

    }

    @Override
    public void limparCampos() {
        view.getFieldCodigo().setText("");
        view.getFieldParcela().setText("");
        view.getFieldDescricao().setText("");
        view.getFieldVencimento().setText("");
        view.getFieldValor().setText("");
        
        view.getFieldDataPagamento().setText("");
        view.getFieldValorPago().setText("");
        view.getFieldObservacao().setText("");
        this.movimento = null;

    }
    
    public void aoAbrirFormulario(){
       view.getFieldDataPagamento().setText(view.getFieldVencimento().getText());
       view.getFieldValorPago().setText(view.getFieldValor().getText());
       view.getFieldDataPagamento().requestFocus();
    }
    
    public MovimentoPagamento getMovimento(){
        return this.movimento;
    }
    
    public void inicializarComboBox(){
        
        formaPagamentoService.getTodos()
                .stream()
                .forEach(f -> view.getComboFormaPagamento().addItem(f.getForma()));
    }
}
