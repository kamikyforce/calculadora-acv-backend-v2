package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ConcentradoDietaLoteRequest {

    @NotNull(message = "ID da nutrição do lote é obrigatório")
    private Long nutricaoLoteId;

    @NotBlank(message = "Nome do concentrado é obrigatório")
    @Size(max = 100, message = "Nome do concentrado deve ter no máximo 100 caracteres")
    private String nomeConcentrado;

    @NotNull(message = "Percentual é obrigatório")
    @DecimalMin(value = "0.0", message = "Percentual deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0", message = "Percentual deve ser menor ou igual a 100")
    private BigDecimal percentual;

    @DecimalMin(value = "0.0", message = "Proteína bruta percentual deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0", message = "Proteína bruta percentual deve ser menor ou igual a 100")
    private BigDecimal proteinaBrutaPercentual;

    @Size(max = 50, message = "Uréia deve ter no máximo 50 caracteres")
    private String ureia;

    @Size(max = 255, message = "Subproduto deve ter no máximo 255 caracteres")
    private String subproduto;

    @DecimalMin(value = "0.0", message = "Quantidade deve ser maior ou igual a 0")
    private BigDecimal quantidade;

    @DecimalMin(value = "0.0", message = "Oferta deve ser maior ou igual a 0")
    private BigDecimal oferta;

    @NotNull(message = "Quantidade (kg/cab/d) é obrigatória")
    @DecimalMin(value = "0.0", message = "Quantidade deve ser maior ou igual a 0")
    private BigDecimal quantidadeKgCabDia;

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

    public String getNomeConcentrado() {
        return nomeConcentrado;
    }

    public void setNomeConcentrado(String nomeConcentrado) {
        this.nomeConcentrado = nomeConcentrado;
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

    public BigDecimal getProteinaBrutaPercentual() {
        return proteinaBrutaPercentual;
    }

    public void setProteinaBrutaPercentual(BigDecimal proteinaBrutaPercentual) {
        this.proteinaBrutaPercentual = proteinaBrutaPercentual;
    }

    public String getUreia() {
        return ureia;
    }

    public void setUreia(String ureia) {
        this.ureia = ureia;
    }

    public String getSubproduto() {
        return subproduto;
    }

    public void setSubproduto(String subproduto) {
        this.subproduto = subproduto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getOferta() {
        return oferta;
    }

    public void setOferta(BigDecimal oferta) {
        this.oferta = oferta;
    }
}