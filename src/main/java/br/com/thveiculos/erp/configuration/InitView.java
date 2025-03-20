package br.com.thveiculos.erp.configuration;

import java.awt.EventQueue;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.views.despesas.FormaPagamentoView;

@Component
public class InitView  implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
    	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormaPagamentoView frame = new FormaPagamentoView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

    }

}
