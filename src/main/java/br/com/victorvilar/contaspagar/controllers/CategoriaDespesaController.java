package br.com.victorvilar.contaspagar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import br.com.victorvilar.contaspagar.controllers.SimpleViewController;
import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.entities.interfaces.SimpleEntity;
import br.com.victorvilar.contaspagar.services.interfaces.CategoriaDespesaService;

@Controller
@Lazy
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
