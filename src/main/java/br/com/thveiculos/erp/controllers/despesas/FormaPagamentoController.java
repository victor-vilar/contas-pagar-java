package br.com.thveiculos.erp.controllers.despesas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.views.despesas.FormaPagamentoView;

@Component
public class FormaPagamentoController {

	private FormaPagamentoView view;
	private FormaPagamentoService service;
	
	@Autowired
	public FormaPagamentoController(FormaPagamentoService service) {
		this.service = service;
	}
	
	public FormaPagamentoController(){
		System.out.println("Iniciei...");
	}
	
	public void setView(FormaPagamentoView view) {
		this.view = view;
	}
	
	public void salvar() {
		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setForma(view.getFieldFormaPagamento().getText());
		service.save(formaPagamento);
		atualizarTabela();
	}
	
	public void atualizarTabela() {
		DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
		deleteAllRows(model);
		service.getTodos().stream().forEach(f -> model.addRow(new Object[] {f.getId(),f.getForma()}));
	}
	
	public void deleteAllRows(final DefaultTableModel model) {
	    for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
	        model.removeRow(i);
	    }
	}


		
}
