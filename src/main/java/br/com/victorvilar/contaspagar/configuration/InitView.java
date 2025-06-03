package br.com.victorvilar.contaspagar.configuration;

import br.com.victorvilar.contaspagar.services.implementation.GeradorDatasDespesaRecorrente;
import br.com.victorvilar.contaspagar.services.implementation.GeradorDeMovimentoDespesaRecorrente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.victorvilar.contaspagar.views.MainView;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

@Component
@Profile("!test")
public class InitView implements CommandLineRunner {

    private MainView view;
    private final GeradorDatasDespesaRecorrente gerador;

    @Autowired
    public InitView(MainView view, GeradorDatasDespesaRecorrente gerador) {
        this.view = view; this.gerador = gerador;
    }

    @Override
    public void run(String... args) throws Exception {
        view.setVisible(true);
        lancarDespesasRecorrentes();
    }

    @Async
    public void lancarDespesasRecorrentes(){
        gerador.run();
    }


}
