package br.gov.serpro.calculadoraacv.controller;

import br.gov.serpro.calculadoraacv.dto.AuthStatusResponse;
import br.gov.serpro.calculadoraacv.dto.LoginRequest;
import br.gov.serpro.calculadoraacv.dto.LoginResponse;
import br.gov.serpro.calculadoraacv.dto.UsuarioResponse;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.service.DefaultUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "https://des-calculadora-acv.np.bsa.estaleiro.serpro.gov.br", "http://localhost:8080"}, allowCredentials = "true")
public class PublicAuthController {

    private final DefaultUserService defaultUserService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginPublico(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // For development: always return success with default user
        Usuario defaultUser = defaultUserService.getOrCreateDefaultUser();
        UsuarioResponse userResponse = defaultUserService.toUsuarioResponse(defaultUser);
        
        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("authenticated", true);
        session.setAttribute("userId", defaultUser.getId());
        session.setAttribute("userCpf", defaultUser.getCpf());
        
        // Create response
        LoginResponse response = new LoginResponse();
        response.setToken("mock-jwt-token-" + UUID.randomUUID().toString());
        response.setRefreshToken("mock-refresh-token-" + UUID.randomUUID().toString());
        response.setUser(userResponse);
        response.setExpiresIn(3600L);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPublicoGet() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", "http://localhost:8080/calculadoraacv/backend/oauth2/authorization/govbr");
        response.put("message", "Use window.location.href para redirecionar");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<AuthStatusResponse> statusPublico(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        AuthStatusResponse response = new AuthStatusResponse();
        
        if (session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            Usuario defaultUser = defaultUserService.getOrCreateDefaultUser();
            UsuarioResponse userResponse = defaultUserService.toUsuarioResponse(defaultUser);
            
            response.setAuthenticated(true);
            response.setUser(userResponse);
        } else {
            response.setAuthenticated(false);
            response.setUser(null);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // Check if user has valid session
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            response.put("success", true);
            response.put("message", "Session refreshed successfully");
            return ResponseEntity.ok(response);
        }
        
        response.put("success", false);
        response.put("message", "No valid session found");
        response.put("loginUrl", "http://localhost:8080/calculadoraacv/backend/oauth2/authorization/govbr");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}