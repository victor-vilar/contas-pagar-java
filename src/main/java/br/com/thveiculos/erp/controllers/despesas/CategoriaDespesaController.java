package br.com.thveiculos.erp.controllers.despesas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.thveiculos.erp.controllers.SimpleViewController;
import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;
import br.com.thveiculos.erp.entities.interfaces.SimpleEntity;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;

@Controller
public class CategoriaDespesaController extends SimpleViewController {

	
	@Autowired
	public CategoriaDespesaController(CategoriaDespesaService service) {
		super();
		this.service = service;
	}


	
	@Override
	public SimpleEntity construirObjeto() {
		
		CategoriaDespesa obj = new CategoriaDespesa();
		obj.setCategoria(view.getFieldNome().getText().toUpperCase());
		
		if(!view.getFieldId().getText().equals("")) {
			obj.setId(Long.valueOf(view.getFieldId().getText()));
		}
		
		return obj;
	}


	
	
	
}
