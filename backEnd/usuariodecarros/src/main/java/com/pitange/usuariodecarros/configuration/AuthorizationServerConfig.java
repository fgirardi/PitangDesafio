package com.pitange.usuariodecarros.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

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
		
	@Bean
	public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
		
		return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
		
	}
	
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}
