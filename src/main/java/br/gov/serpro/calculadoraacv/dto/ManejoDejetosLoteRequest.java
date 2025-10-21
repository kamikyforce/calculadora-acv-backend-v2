package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ManejoDejetosLoteRequest {

    @NotNull(message = "ID do lote é obrigatório")
    private Long loteId;

    @NotBlank(message = "Categoria do animal é obrigatória")
    @Size(max = 100, message = "Categoria do animal deve ter no máximo 100 caracteres")
    private String categoriaAnimal;

    @NotBlank(message = "Tipo de manejo é obrigatório")
    @Size(max = 100, message = "Tipo de manejo deve ter no máximo 100 caracteres")
    private String tipoManejo;

    @NotNull(message = "Percentual do rebanho é obrigatório")
    @DecimalMin(value = "0.0", message = "Percentual do rebanho deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0", message = "Percentual do rebanho deve ser menor ou igual a 100")
    private BigDecimal percentualRebanho;

    // Getters and Setters
    public Long getLoteId() {
        return loteId;
    }

    public void setLoteId(Long loteId) {
        this.loteId = loteId;
    }

    public String getCategoriaAnimal() {
        return categoriaAnimal;
    }

    public void setCategoriaAnimal(String categoriaAnimal) {
        this.categoriaAnimal = categoriaAnimal;
    }

    public String getTipoManejo() {
        return tipoManejo;
    }

    public void setTipoManejo(String tipoManejo) {
        this.tipoManejo = tipoManejo;
    }

    public BigDecimal getPercentualRebanho() {
        return percentualRebanho;
    }

    public void setPercentualRebanho(BigDecimal percentualRebanho) {
        this.percentualRebanho = percentualRebanho;
    }
}