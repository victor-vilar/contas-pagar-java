package br.com.thveiculos.erp;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "br.com.thveiculos.erp")
public class ErpApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ErpApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
		
		//SpringApplication.run(ErpApplication.class, args);
	}

}
