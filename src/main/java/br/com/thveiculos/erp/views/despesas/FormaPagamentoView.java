package br.com.thveiculos.erp.views.despesas;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//import br.com.thveiculos.erp.configuration.ApplicationConfiguration;
import br.com.thveiculos.erp.controllers.despesas.FormaPagamentoController;
import br.com.thveiculos.erp.views.SimpleView;

@Component
@Lazy
public class FormaPagamentoView extends SimpleView {
	
	public FormaPagamentoView(FormaPagamentoController controller) {
		super("Formas Pagamento", "Forma");
		this.controller = controller;
		controller.setView(this);
		
	}
}
