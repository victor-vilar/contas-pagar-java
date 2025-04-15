package br.com.victorvilar.contaspagar;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import javax.swing.UIManager;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ErpApplication {

    public static void main(String[] args) {
        setLookAndFeel();

        new SpringApplicationBuilder(ErpApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);

        //SpringApplication.run(ErpApplication.class, args);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showHorizontalLines",true);
            UIManager.put("Table.alternateRowColor", Color.decode("#f7f3f2"));

        } catch (Exception ex) {
            System.out.println(ex);

        }
    }

}
