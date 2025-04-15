package br.com.victorvilar.contaspagar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.victorvilar.contaspagar.views.MainView;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

@Component
public class InitView implements CommandLineRunner {

    private MainView view;

    @Autowired
    public InitView(MainView view) {
        this.view = view;
    }

    @Override
    public void run(String... args) throws Exception {
        view.setVisible(true);
    }



}
