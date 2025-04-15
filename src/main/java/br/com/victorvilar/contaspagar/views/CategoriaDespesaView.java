package br.com.victorvilar.contaspagar.views;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.victorvilar.contaspagar.controllers.CategoriaDespesaController;
import br.com.victorvilar.contaspagar.views.SimpleView;

@Component
@Lazy
public class CategoriaDespesaView extends SimpleView {

	public CategoriaDespesaView(CategoriaDespesaController controller) {
		super("Categoria Despesas", "Categoria");
		this.controller = controller;
		controller.setView(this);

	}
}
