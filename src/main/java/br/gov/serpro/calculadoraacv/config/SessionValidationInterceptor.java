package br.gov.serpro.calculadoraacv.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@Slf4j
public class SessionValidationInterceptor implements HandlerInterceptor {

    private final List<String> publicPaths = List.of(
            "/calculadoraacv/backend/public",
            "/auth",
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator/health",
            "/administradores",
            "/certificadoras",
            "/industrias",
            "/usuarios",
            "/rebanho",
            "/insumos-rebanho",
            "/insumos-producao-agricola"
    );

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if (isPublicPath(requestURI)) {
            return true;
        }

        // Check for session-based authentication first
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            log.info("Acesso autorizado para URI: {} - Usuário autenticado via sessão", requestURI);
            return true;
        }

        // Then check for OAuth2 authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && 
            !(auth instanceof AnonymousAuthenticationToken)) {
        
            if (auth.getPrincipal() instanceof OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                log.info("Acesso autorizado para URI: {} - Usuário OAuth2: {}", requestURI, email);
                return true;
            }
        }

        // If neither session nor OAuth2 authentication is valid, deny access
        log.warn("Acesso negado para URI: {} - Usuário não autenticado", requestURI);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Usuário não autenticado\",\"code\":401}");
        return false;
    }

    private boolean isPublicPath(String requestURI) {
        return publicPaths.stream().anyMatch(requestURI::startsWith);
    }
}