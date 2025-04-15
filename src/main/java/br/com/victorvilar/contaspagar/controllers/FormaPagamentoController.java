package br.com.victorvilar.contaspagar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import br.com.victorvilar.contaspagar.controllers.SimpleViewController;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.interfaces.SimpleEntity;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import org.springframework.stereotype.Controller;

@Controller
@Lazy
public class FormaPagamentoController extends SimpleViewController {
	
	@Autowired
	public FormaPagamentoController(FormaPagamentoService service) {
		super();
		this.service = service;
	}

	@Override
	public SimpleEntity construirObjeto() {
		
		FormaPagamento obj = new FormaPagamento();
		obj.setForma(view.getFieldNome().getText().toUpperCase());
		
		if(!view.getFieldId().getText().equals("")) {
			obj.setId(Long.valueOf(view.getFieldId().getText()));
		}
		
		return obj;
	}



	

	



		
}
