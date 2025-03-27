package br.com.thveiculos.erp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.views.util.MainView;
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
        setLookAndFeel();
        view.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
                UIManager.setLookAndFeel( new FlatLightLaf() );
        }catch (Exception ex) {
                System.out.println(ex);
                
        }
    }

}
