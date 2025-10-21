package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class AdministradorResponse {
    private Long id;
    private String orgao;
    private UsuarioResponse usuario;
    private String perfil;
}
