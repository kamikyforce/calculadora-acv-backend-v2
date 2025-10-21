package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String cpf;
    private String senha;
    private Boolean manterConectado;
}