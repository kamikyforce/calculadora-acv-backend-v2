package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.AuthStatusResponse;
import br.gov.serpro.calculadoraacv.dto.PermissaoResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioResponse;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.service.DefaultUserService;
import br.gov.serpro.calculadoraacv.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br"}, allowCredentials = "true")
public class AuthController {

    private final UsuarioService usuarioService;
    private final DefaultUserService defaultUserService;
    
    @Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;
    
    @Value("${server.servlet.context-path:/calculadoraacv/backend}")
    private String contextPath;
    
    @Value("${server.port:8080}")
    private String serverPort;

    // Endpoint que retorna a URL de login para redirecionamento no frontend
    @GetMapping("/login-url")
    public ResponseEntity<Map<String, String>> obterUrlLogin() {
        Map<String, String> response = new HashMap<>();
        String baseUrl = "http://localhost:" + serverPort + contextPath;
        response.put("loginUrl", contextPath + "/oauth2/authorization/govbr");
        response.put("fullUrl", baseUrl + "/oauth2/authorization/govbr");
        response.put("message", "IMPORTANTE: Use window.location.href para redirecionar. NÃO use fetch()!");
        response.put("error", "OAuth2 não pode ser acessado via fetch() devido a políticas CORS");
        return ResponseEntity.ok(response);
    }

    // Endpoint direto de login (redireciona automaticamente)
    @GetMapping("/login")
    public void iniciarLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/calculadoraacv/backend/oauth2/authorization/govbr");
    }

    // Novo endpoint para explicar como fazer login corretamente via POST
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> explicarLoginPost() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "OAuth2 não pode ser acessado via POST/fetch()");
        response.put("solution", "Use window.location.href = 'http://localhost:8080/calculadoraacv/backend/oauth2/authorization/govbr'");
        response.put("reason", "OAuth2 requer redirecionamento direto do navegador, não AJAX");
        response.put("loginUrl", "http://localhost:8080/calculadoraacv/backend/oauth2/authorization/govbr");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Sessão finalizada com sucesso");
        response.put("redirectUrl", frontendUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> usuarioAutenticado(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getAttribute("email");
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId().toString());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setTipo(usuario.getTipo());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/permissoes")
    public ResponseEntity<PermissaoResponse> obterPermissoes(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getAttribute("email");
        Usuario usuario = usuarioService.buscarPorEmail(email);

        PermissaoResponse permissoes = usuarioService.obterPermissoes(usuario);
        return ResponseEntity.ok(permissoes);
    }

    @GetMapping("/status")
    public ResponseEntity<AuthStatusResponse> verificarStatus(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        AuthStatusResponse response = new AuthStatusResponse();
        
        // Check session first (for default user)
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            Usuario defaultUser = defaultUserService.getOrCreateDefaultUser();
            UsuarioResponse userResponse = defaultUserService.toUsuarioResponse(defaultUser);
            
            response.setAuthenticated(true);
            response.setUser(userResponse);
            return ResponseEntity.ok(response);
        }
        
        // Check OAuth2 authentication
        if (principal == null) {
            response.setAuthenticated(false);
            response.setUser(null);
            return ResponseEntity.ok(response);
        }
        
        String email = principal.getAttribute("email");
        Usuario usuario = usuarioService.buscarPorEmail(email);
        
        if (usuario == null) {
            response.setAuthenticated(false);
            response.setUser(null);
            return ResponseEntity.ok(response);
        }
        
        UsuarioResponse userResponse = defaultUserService.toUsuarioResponse(usuario);
        response.setAuthenticated(true);
        response.setUser(userResponse);
        
        return ResponseEntity.ok(response);
    }
}
