package com.pitange.usuariodecarros;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;


@SpringBootApplication()
public class UsuariodecarrosApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UsuariodecarrosApplication.class, args);
	}

}
