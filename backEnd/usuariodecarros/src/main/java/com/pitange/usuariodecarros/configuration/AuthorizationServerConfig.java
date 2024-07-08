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
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
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
    
    // Injetando o valor da propriedade 'security.clientID' definida em um arquivo de propriedades
    @Value("${security.clientID}")
    private String clientID;

    // Configura e registra clientes autorizados no servidor de autorização usando um repositório em memória
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // Cria um cliente registrado com ID, clientID, clientSecret, métodos de autenticação, escopos e tipos de concessão
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

        // Retorna um repositório em memória contendo o cliente registrado
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    // Configura as definições do provedor de autorização, incluindo o URI do emissor
    @Bean
    public AuthorizationServerSettings  providerSettings(AuthProperties authProperties) {
        return AuthorizationServerSettings .builder()
                .issuer(authProperties.getProviderUri())
                .build();
    }
    
    // Configura o conjunto de chaves JWK carregando uma chave RSA de um arquivo JKS
    @Bean
    public JWKSet jwSset(AuthProperties authProperties) throws Exception {
        final var jksProperties = authProperties.getJks();
        final String jksPath = authProperties.getJks().getPath();
        final InputStream inputStream = new ClassPathResource(jksPath).getInputStream();
        
        // Carrega o KeyStore do arquivo JKS
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());
            
        // Carrega a chave RSA do KeyStore
        RSAKey rsakey = RSAKey.load(keyStore, 
                                    jksProperties.getAlias(), 
                                    jksProperties.getKeypass().toCharArray());
        
        // Retorna um conjunto de chaves JWK contendo a chave RSA carregada
        return new JWKSet(rsakey);
    }
    
    // Configura a fonte de chaves JWK para seleção de chaves baseada em contexto de segurança
    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }
    
    // Configura o codificador JWT usando a fonte de chaves JWK configurada
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
