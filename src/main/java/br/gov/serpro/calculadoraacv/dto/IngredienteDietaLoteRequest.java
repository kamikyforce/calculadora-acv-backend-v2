package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class IngredienteDietaLoteRequest {

    @NotNull(message = "ID da nutrição do lote é obrigatório")
    private Long nutricaoLoteId;

    @NotBlank(message = "Nome do ingrediente é obrigatório")
    @Size(max = 100, message = "Nome do ingrediente deve ter no máximo 100 caracteres")
    private String nomeIngrediente;

    @DecimalMin(value = "0.0", message = "Percentual deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0", message = "Percentual deve ser menor ou igual a 100")
    private BigDecimal percentual;

    @NotNull(message = "Quantidade (kg/cab/d) é obrigatória")
    @DecimalMin(value = "0.0", message = "Quantidade deve ser maior ou igual a 0")
    private BigDecimal quantidadeKgCabDia;

    @NotNull(message = "Oferta (dias/ano) é obrigatória")
    @Min(value = 0, message = "Oferta deve ser maior ou igual a 0")
    private Integer ofertaDiasAno;

    @NotNull(message = "Origem da produção é obrigatória")
    private OrigemProducao producao;

    // Getters and Setters
    public Long getNutricaoLoteId() {
        return nutricaoLoteId;
    }

    public void setNutricaoLoteId(Long nutricaoLoteId) {
        this.nutricaoLoteId = nutricaoLoteId;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public BigDecimal getQuantidadeKgCabDia() {
        return quantidadeKgCabDia;
    }

    public void setQuantidadeKgCabDia(BigDecimal quantidadeKgCabDia) {
        this.quantidadeKgCabDia = quantidadeKgCabDia;
    }

    public Integer getOfertaDiasAno() {
        return ofertaDiasAno;
    }

    public void setOfertaDiasAno(Integer ofertaDiasAno) {
        this.ofertaDiasAno = ofertaDiasAno;
    }

    public OrigemProducao getProducao() {
        return producao;
    }

    public void setProducao(OrigemProducao producao) {
        this.producao = producao;
    }
}