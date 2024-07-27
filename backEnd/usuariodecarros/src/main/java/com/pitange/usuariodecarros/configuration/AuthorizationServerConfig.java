package com.pitange.usuariodecarros.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.pitange.usuariodecarros.properties.AuthProperties;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.UUID;

/**
 * Configuração do servidor de autorização.
 */
@Configuration
public class AuthorizationServerConfig {
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
    private JdbcOperations jdbcOperations;
	
    // Injetando o valor da propriedade 'security.clientID' definida em um arquivo de propriedades
    @Value("${security.clientID}")
    private String clientID;

    /**
     * Configura e registra clientes autorizados no servidor de autorização usando um repositório em memória.
     *
     * @return o repositório de clientes registrados em memória
     * AuthorizationGrantType.AUTHORIZATION_CODE
		Descrição: 
			O fluxo de Autorização por Código é um dos mais seguros e comuns fluxos de autorização usados pelo OAuth 2.0.
		Como funciona:
			Etapa de Autorização:
			O usuário tenta acessar um recurso protegido (como um aplicativo).
			O usuário é redirecionado para um servidor de autorização (normalmente um servidor de autenticação) 
			onde ele faz login.
			Após o login, o servidor de autorização redireciona o usuário de volta ao aplicativo com um código de autorização.
			Troca do Código por um Token:
			O aplicativo recebe o código de autorização e o troca por um token de acesso ao recurso protegido.
		Vantagens:
			Maior segurança, pois o token de acesso nunca é exposto diretamente ao usuário ou ao navegador.
			Permite que o servidor de autorização valide diretamente o cliente e o código de autorização.
		
		AuthorizationGrantType.REFRESH_TOKEN
		Descrição: 
			O Refresh Token é usado para obter novos tokens de acesso sem a necessidade de reautenticar o usuário.
		Como funciona:
			Quando um token de acesso expira, o cliente pode usar um token de atualização (refresh token) 
			para solicitar um novo token de acesso.
			O cliente envia o refresh token ao servidor de autorização.
			O servidor de autorização valida o refresh token e, se válido, emite um novo token de acesso.
		Vantagens:
			Melhora a experiência do usuário, pois não é necessário fazer login repetidamente.
			Reduz a exposição do token de acesso, pois o token de atualização pode ter um tempo de vida maior e ser utilizado 
			apenas para renovar tokens de acesso.
			
		AuthorizationGrantType.CLIENT_CREDENTIALS
		Descrição: 
			O fluxo de Credenciais do Cliente é usado quando um cliente (como um serviço ou aplicativo backend) precisa acessar seus próprios recursos, em vez de acessar recursos em nome de um usuário.
		Como funciona:
			O cliente envia suas credenciais (ID do cliente e segredo do cliente) diretamente ao servidor de autorização.
			O servidor de autorização valida as credenciais do cliente.
			Se as credenciais forem válidas, o servidor emite um token de acesso.
		Vantagens:
			Útil para comunicação entre servidores, onde não há um usuário humano envolvido.
			Simplifica o processo de autenticação para aplicativos que precisam acessar recursos diretamente.
			
		Exemplos Práticos
			Authorization Code: 
				Imagine um aplicativo web que permite login com o Google. Quando o usuário tenta acessar o aplicativo, 
				ele é redirecionado para a página de login do Google. Após o login, o Google redireciona o usuário de volta ao 
				aplicativo com um código de autorização. O aplicativo então usa esse código para obter um token de acesso e 
				acessar os dados do usuário.
			Refresh Token: 
				Após o usuário se logar no aplicativo, o aplicativo recebe um token de acesso com validade de 1 hora 
				e um refresh token com validade de 30 dias. Quando o token de acesso expira, o aplicativo usa o refresh token 
				para obter um novo token de acesso sem exigir que o usuário faça login novamente.
			Client Credentials: 
				Um serviço de backend que processa pagamentos precisa acessar uma API de pagamento. 
				Ele usa suas credenciais (ID e segredo do cliente) para obter um token de acesso e 
				fazer chamadas à API diretamente.
     */
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
                .redirectUri("https://localhost:8080/redirect")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofDays(30)).build())
                .clientSettings(ClientSettings.builder().build()).build();
        
        
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcOperations);
        registeredClientRepository.save(registeredClient);
        return registeredClientRepository;
    }

    /**
     * Configura as definições do provedor de autorização, incluindo o URI do emissor.
     *
     * @param authProperties as propriedades de autenticação
     * @return as configurações do servidor de autorização
     */
    @Bean
    public AuthorizationServerSettings providerSettings(AuthProperties authProperties) {
    	
    	System.out.println("================================================");
    	System.out.println("ProviderUri: " + authProperties.getProviderUri());
    	System.out.println("================================================");
    	
    	return AuthorizationServerSettings.builder()
                .issuer(authProperties.getProviderUri())
                .build();
    }
    
    /**
     * Configura o conjunto de chaves JWK carregando uma chave RSA de um arquivo JKS.
     *
     * @param authProperties as propriedades de autenticação
     * @return o conjunto de chaves JWK
     * @throws Exception se ocorrer um erro ao carregar a chave
     */
    @Bean
    public JWKSet jwkSet(AuthProperties authProperties) throws Exception {
        final var jksProperties = authProperties.getJks();
        final String jksPath = authProperties.getJks().getPath();
        final InputStream inputStream = new ClassPathResource(jksPath).getInputStream();
        
        // Carrega o KeyStore do arquivo JKS
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());
            
        // Carrega a chave RSA do KeyStore
        RSAKey rsaKey = RSAKey.load(keyStore, 
                                    jksProperties.getAlias(), 
                                    jksProperties.getKeypass().toCharArray());
        
        // Retorna um conjunto de chaves JWK contendo a chave RSA carregada
        return new JWKSet(rsaKey);
    }
    
    /**
     * Configura a fonte de chaves JWK para seleção de chaves baseada em contexto de segurança.
     *
     * @param jwkSet o conjunto de chaves JWK
     * @return a fonte de chaves JWK
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }
    
    /**
     * Configura o codificador JWT usando a fonte de chaves JWK configurada.
     *
     * @param jwkSource a fonte de chaves JWK
     * @return o codificador JWT
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
