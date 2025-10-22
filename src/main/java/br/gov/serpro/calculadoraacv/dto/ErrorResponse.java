package br.gov.serpro.calculadoraacv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String codigo;
    private String mensagem;
    private Long idExistente;

    // Construtor de conveniência para compatibilidade com usos existentes
    public ErrorResponse(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }
}
