package br.com.victorvilar.contaspagar.views;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//import br.com.thveiculos.erp.configuration.ApplicationConfiguration;
import br.com.victorvilar.contaspagar.controllers.FormaPagamentoController;
import br.com.victorvilar.contaspagar.views.SimpleView;

@Component
@Lazy
public class FormaPagamentoView extends SimpleView {
	
	public FormaPagamentoView(FormaPagamentoController controller) {
		super("Formas Pagamento", "Forma");
		this.controller = controller;
		controller.setView(this);
		
	}
}
