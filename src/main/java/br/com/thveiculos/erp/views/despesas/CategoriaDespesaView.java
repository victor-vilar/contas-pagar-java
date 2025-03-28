package br.com.thveiculos.erp.views.despesas;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.controllers.despesas.CategoriaDespesaController;
import br.com.thveiculos.erp.views.SimpleView;

@Component
@Lazy
public class CategoriaDespesaView extends SimpleView {

	public CategoriaDespesaView(CategoriaDespesaController controller) {
		super("Categoria Despesas", "Categoria");
		this.controller = controller;
		controller.setView(this);

	}
}
