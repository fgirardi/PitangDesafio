package com.pitange.usuariodecarros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;


@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class UsuariodecarrosApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UsuariodecarrosApplication.class, args);
	}

}
