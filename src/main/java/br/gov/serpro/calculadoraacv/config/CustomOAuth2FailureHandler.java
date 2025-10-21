package br.gov.serpro.calculadoraacv.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.*;

import java.io.IOException;

@Component
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        
        log.error("Falha na autenticação OAuth2: {}", exception.getMessage());
        
        // Redirecionar para página de erro usando URL configurável
        String errorUrl = frontendUrl + "/login?error=oauth_failure";
        
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(errorUrl);
    }
}