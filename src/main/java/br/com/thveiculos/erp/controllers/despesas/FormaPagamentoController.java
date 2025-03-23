package br.com.thveiculos.erp.controllers.despesas;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.controllers.util.ControllerHelper;
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
	
	
	public void setView(FormaPagamentoView view) {
		this.view = view;
	}
	
	public void novo() {
		view.getFieldFormaPagamento().setEditable(true);
		view.getFieldFormaPagamento().requestFocus();
	}
	
	public void salvar() {
		
		if(view.getFieldFormaPagamento().getText().equals("")) {
			return;
		}
		
		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setForma(view.getFieldFormaPagamento().getText().toUpperCase());
		
		if(!view.getFieldId().getText().equals("")) {
			formaPagamento.setId(Long.valueOf(view.getFieldId().getText()));
			service.update(formaPagamento);
		}else {
			service.save(formaPagamento);
		}
		
		updateView();
	}
	
	public void deletar() {
		
		if(view.getTable().getSelectedRow() == -1) {
			return;
		}
		
		int row = view.getTable().getSelectedRow();
		Long id = (Long)view.getTable().getValueAt(row, 0);
		service.deleteById(id);
		
		updateView();
	}
	
	public void updateView() {
		atualizarTabela();
		limparCampos();
		view.getFieldFormaPagamento().setEditable(false);
	}
	
	
	public void atualizarTabela() {
		DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
		ControllerHelper.limparTabela(model);
		service.getTodos().stream().forEach(f -> model.addRow(new Object[] {f.getId(),f.getForma()}));
	}
	
	public void limparCampos() {
		view.getFieldId().setText(null);
		view.getFieldFormaPagamento().setText(null);
		
	}
	

	

	

	



		
}
