package br.com.thveiculos.erp.configuration;

import java.awt.EventQueue;

import br.com.thveiculos.erp.controllers.despesas.FormaPagamentoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.views.despesas.FormaPagamentoView;

@Component
public class InitView  implements CommandLineRunner {

	
	private FormaPagamentoView view;
	
	@Autowired
	public InitView(FormaPagamentoView view) {
		this.view = view;
	}
	
	@Override
	public void run(String... args) throws Exception {
		view.setVisible(true);
    }

}
