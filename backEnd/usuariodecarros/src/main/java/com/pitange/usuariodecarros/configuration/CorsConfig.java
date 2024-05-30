package com.pitange.usuariodecarros.configuration;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class to register a CORS filter for the application.
 */
@Configuration
public class CorsConfig {

    /**
     * Registers a CORS filter bean that allows cross-origin requests with specific configurations.
     * 
     * @return A FilterRegistrationBean that registers the CORS filter with the highest precedence.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        // Create a new CorsConfiguration object to hold the CORS settings
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Allow requests from any origin by setting allowed origins to "*"
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));

        // Allow any header in the requests by setting allowed headers to "*"
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        // Allow specific HTTP methods (POST, PUT, GET, OPTIONS, DELETE, PATCH)
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH"));

        // Allow credentials (cookies, authorization headers, etc.) to be included in requests
        corsConfiguration.setAllowCredentials(true);

        // Create a source object to hold the URL mapping for the CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Register the CORS configuration for all URL patterns (/**)
        source.registerCorsConfiguration("/**", corsConfiguration);

        // Create a new CorsFilter with the specified configuration source
        CorsFilter corsFilter = new CorsFilter((CorsConfigurationSource) source);

        // Create a FilterRegistrationBean to register the CorsFilter
        FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<>(corsFilter);

        // Set the order of the filter to the highest precedence
        /*
         * Garante que ele será aplicado antes de outros filtros. No caso específico de CORS, 
         * isso é importante porque as configurações de CORS devem ser verificadas e aplicadas antes de qualquer outra lógica 
         * de aplicação que possa ser definida em outros filtros
         */
        
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        // Return the FilterRegistrationBean
        return filter;
    }
}
