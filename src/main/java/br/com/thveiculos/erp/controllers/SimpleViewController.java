package br.com.thveiculos.erp.controllers;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import br.com.thveiculos.erp.controllers.util.ControllerHelper;
import br.com.thveiculos.erp.entities.interfaces.SimpleEntity;
import br.com.thveiculos.erp.services.despesas.interfaces.AppService;
import br.com.thveiculos.erp.views.SimpleView;
import br.com.thveiculos.erp.views.despesas.CategoriaDespesaView;
import br.com.thveiculos.erp.views.despesas.FormaPagamentoView;

/** 
 * Controlador para as views do tipo {@link SimpleView}, esses formulários são objetos que manipulam objetos do tipo {@link SimpleEntity}.As views {@link FormaPagamentoView} e {@link CategoriaDespesaView}
 * são exemplos de formulários que extendem essa classe.
 * As entidades ao qual elas manipulam possuem somente o id e o nome como propriedades então os formulários e suas carecteristicas e layout seguem o mesmo padrão. Essa entidades são conhecidas nesse
 * sistema como 
 
 *Essas views controlam entidades que se comportam como um ENUM, como quero criar a possibilidade do usuário conseguir registrar mais
 *entidades desse tipo, foram criados tabelas no banco de dados para que assim elas possam ser registradas e removidas como qualquer outra classe.
 
 * */
public abstract class SimpleViewController implements AppViewController<SimpleView> {

	protected SimpleView view;
	protected AppService service;
	
	public SimpleViewController() {
	
	}
	
        @Override
	public void setView(SimpleView view) {
		this.view = view;
	}
	
	@Override
	public void novo() {
                view.getFieldId().setText("");
                view.getFieldNome().setText("");
		view.getFieldNome().setEditable(true);
		view.getFieldNome().requestFocus();
	}
	
        @Override
	public void salvar() {
		
		if(view.getFieldNome().getText().equals("")) {
			return;
		}
		
		service.save(construirObjeto());
		
		updateView();
	}
	
        @Override
	public void deletar() {
		
		if(view.getTable().getSelectedRow() == -1) {
			return;
		}
		
		int row = view.getTable().getSelectedRow();
		Long id = (Long)view.getTable().getValueAt(row, 0);
		service.deleteById(id);
		
		updateView();
	}
        
        @Override
	public void limparCampos() {
		view.getFieldId().setText(null);
		view.getFieldNome().setText(null);
		
	}
	
	public void updateView() {
		atualizarTabela();
		limparCampos();
		view.getFieldNome().setEditable(false);
	}
	
	public void atualizarTabela() {
		DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
		ControllerHelper.limparTabela(model);
		List<SimpleEntity> entities =  service.getTodos();
		entities.stream().forEach(f -> model.addRow(new Object[] {f.getId(),f.getName()}));
	}
	
	public abstract SimpleEntity construirObjeto();
	
}
