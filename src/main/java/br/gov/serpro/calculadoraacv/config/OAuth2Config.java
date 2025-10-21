package br.gov.serpro.calculadoraacv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
public class OAuth2Config {

    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository, "/oauth2/authorization");
        
        // Configurar PKCE e outros parâmetros obrigatórios
        authorizationRequestResolver.setAuthorizationRequestCustomizer(
                OAuth2AuthorizationRequestCustomizers.withPkce());
        
        // Adicionar nonce obrigatório
        authorizationRequestResolver.setAuthorizationRequestCustomizer(
                authorizationRequestCustomizer -> 
                    authorizationRequestCustomizer
                        .additionalParameters(params -> {
                            if (!params.containsKey("nonce")) {
                                params.put("nonce", generateNonce());
                            }
                        })
        );
        
        return authorizationRequestResolver;
    }
    
    private String generateNonce() {
        // Gerar nonce aleatório de pelo menos 32 caracteres
        return java.util.UUID.randomUUID().toString().replace("-", "") + 
               java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}