package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IngredienteDietaLoteResponse {

    private Long id;
    private Long nutricaoLoteId;
    private String nomeIngrediente;
    private BigDecimal percentual;
    private BigDecimal quantidadeKgCabDia;
    private Integer ofertaDiasAno;
    private OrigemProducao producao;
    private LocalDateTime dataCriacao;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}