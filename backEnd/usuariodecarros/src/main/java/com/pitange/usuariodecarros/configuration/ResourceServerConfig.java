package com.pitange.usuariodecarros.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pitange.usuariodecarros.service.UserService;

/**
 * Configura a segurança da aplicação usando Spring Security.
 */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Autowired
    private SecurityFilter securityFilter;
    
    @Autowired
    private UserService userService;
    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService());
        authProvider.setPasswordEncoder(this.passwordEncoder());
        return authProvider;
    }

    /**
     * Obtém o gerenciador de autenticação a partir da configuração de autenticação.
     *
     * @param authenticationConfiguration A configuração de autenticação do Spring Security.
     * @return O gerenciador de autenticação.
     * @throws Exception Se ocorrer um erro ao obter o gerenciador de autenticação.
     */
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return new ProviderManager(authenticationProvider());
    }
     
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
    	return this.userService;
    }

    
    /**
     * Configura a cadeia de filtros de segurança HTTP.
     *
     * @param http O objeto HttpSecurity a ser configurado.
     * @return O objeto SecurityFilterChain configurado.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	
        // Configura CORS com as configurações padrão
        http.cors(withDefaults())
            // Desabilita a proteção CSRF, pois não estamos usando uma aplicação web tradicional
            .csrf(csrf -> csrf.disable())
            // Define a política de criação de sessão como STATELESS (sem estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configura as autorizações de requisições HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite todas as requisições POST para o endpoint /login
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                // Permite requisições POST para /api/users/** apenas para usuários com os papéis ADMIN ou USER
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
               // Permite acesso ao endpoint de token
                .requestMatchers(HttpMethod.POST,"/oauth2/**").permitAll() 
                // Permite acesso ao console do H2
                .requestMatchers("/h2-console/**").permitAll()
                // Requer autenticação para qualquer outra requisição
                .anyRequest().authenticated())
                 // Adiciona o filtro de segurança customizado antes do filtro padrão de autenticação de username e senha
            	.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            	.oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
            	// Configura a funcionalidade de logout e permite acesso público a ela
            	
            	.logout(logout -> logout.permitAll());

        		// Permite que o console do H2 seja exibido em um frame
        		http.headers().frameOptions().disable();
  
        // Retorna a cadeia de filtros de segurança configurada
        return http.build();
    }


    
    
   
    
}
