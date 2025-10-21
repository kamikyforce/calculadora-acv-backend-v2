package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.OrigemProducao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConcentradoDietaLoteResponse {

    private Long id;
    private Long nutricaoLoteId;
    private String nomeConcentrado;
    private BigDecimal percentual;
    private BigDecimal proteinaBrutaPercentual;
    private String ureia;
    private String subproduto;
    private BigDecimal quantidade;
    private BigDecimal oferta;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
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