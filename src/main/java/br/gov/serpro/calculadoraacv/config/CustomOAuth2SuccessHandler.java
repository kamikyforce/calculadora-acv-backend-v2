package br.gov.serpro.calculadoraacv.config;

import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService service;
    
    @Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        
        log.info("Login OAuth2 bem-sucedido para: {}", email);

        Usuario existente = service.buscarPorEmail(email);
        if (existente == null) {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNome(nome);
            usuario.setTipo(TipoUsuario.ADMINISTRADOR);
            service.salvar(usuario);
            log.info("Novo usuário criado: {}", email);
        }

        // Redirecionar para o frontend após login bem-sucedido
        String redirectUrl = frontendUrl + "/?login=success";
        log.info("Redirecionando para: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}