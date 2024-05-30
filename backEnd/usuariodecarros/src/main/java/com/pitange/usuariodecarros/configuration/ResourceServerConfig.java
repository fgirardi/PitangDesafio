package com.pitange.usuariodecarros.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {
	
	@Autowired
	SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.cors(withDefaults()).csrf(csrf -> csrf.disable()) // Nao estamos usando uma aplicacao web dentro do
																// springboot, entao disable
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN", "USER").anyRequest()
						.authenticated())
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(formLogin -> formLogin.loginPage("/login").permitAll()).logout(logout -> logout.permitAll());

		return http.build();
	}

}
