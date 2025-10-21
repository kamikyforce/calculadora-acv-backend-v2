package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioCertificadoraRequest {
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String dataCadastro;
    
    private Boolean ativo = true;
}