package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class AuthStatusResponse {
    private Boolean authenticated;
    private UsuarioResponse user;
}