package br.com.victorvilar.contaspagar;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import br.com.victorvilar.contaspagar.configuration.LookAndFellConfiguration;

@SpringBootApplication
public class ErpApplication {

    public static void main(String[] args) {
        LookAndFellConfiguration.setLookAndFeel();

        new SpringApplicationBuilder(ErpApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);

        //SpringApplication.run(ErpApplication.class, args);
    }



}
