package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UsuarioResponse user;
    private Long expiresIn;
}