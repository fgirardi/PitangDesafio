package com.pitange.usuariodecarros.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import static org.springframework.security.config.Customizer.withDefaults;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {
    
    @Value("${security.clientID}")
    private String clientID;
    

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId(this.clientID)
				.clientSecret("{bcrypt}" + new BCryptPasswordEncoder().encode(this.clientID))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
				.scope("read")
				.scope("write")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1))
						.refreshTokenTimeToLive(Duration.ofDays(30)).build())
				.clientSettings(ClientSettings.builder().build()).build();

		return new InMemoryRegisteredClientRepository(registeredClient);
	}

	@Bean
	public ProviderSettings providerSettings(AuthProperties authProperties) {
		
		return ProviderSettings.builder()
				.issuer(authProperties.getProviderUri())
				.build();
	}
	
	@Bean
	public JWKSet jwSset(AuthProperties authProperties) throws Exception {
		final var jksProperties = authProperties.getJks();
		final String jksPath = authProperties.getJks().getPath();
		final InputStream inputStream = new ClassPathResource(jksPath)
											.getInputStream();
		
		final KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());
			
		RSAKey rsakey = RSAKey.load(keyStore, 
									jksProperties.getAlias(), 
									jksProperties.getKeypass().toCharArray());
		
		return new JWKSet(rsakey);
	
	}
		
	
}
