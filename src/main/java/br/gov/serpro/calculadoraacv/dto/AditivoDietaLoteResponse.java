package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AditivoDietaLoteResponse {

    private Long id;
    private Long nutricaoLoteId;
    private String nomeAditivo;
    private BigDecimal percentual;
    private String tipo;
    private BigDecimal dose;
    private BigDecimal oferta;
    private BigDecimal percentualAdicional;
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

    public String getNomeAditivo() {
        return nomeAditivo;
    }

    public void setNomeAditivo(String nomeAditivo) {
        this.nomeAditivo = nomeAditivo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getDose() {
        return dose;
    }

    public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

    public BigDecimal getOferta() {
        return oferta;
    }

    public void setOferta(BigDecimal oferta) {
        this.oferta = oferta;
    }

    public BigDecimal getPercentualAdicional() {
        return percentualAdicional;
    }

    public void setPercentualAdicional(BigDecimal percentualAdicional) {
        this.percentualAdicional = percentualAdicional;
    }
}