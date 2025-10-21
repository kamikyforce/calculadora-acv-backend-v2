package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class AdministradorRequest {
    private Long usuarioId;
    private String orgao;
    private Long perfilId;
}
