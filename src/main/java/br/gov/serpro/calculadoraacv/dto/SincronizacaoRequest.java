package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import jakarta.validation.constraints.NotNull;

public class SincronizacaoRequest {
    
    @NotNull(message = "ID do combustível é obrigatório")
    private Long combustivelId;
    
    @NotNull(message = "Escopo destino é obrigatório")
    private EscopoEnum escopoDestino;

    // Getters and Setters
    public Long getCombustivelId() { return combustivelId; }
    public void setCombustivelId(Long combustivelId) { this.combustivelId = combustivelId; }
    
    public EscopoEnum getEscopoDestino() { return escopoDestino; }
    public void setEscopoDestino(EscopoEnum escopoDestino) { this.escopoDestino = escopoDestino; }
}