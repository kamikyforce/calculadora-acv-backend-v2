package br.gov.serpro.calculadoraacv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ManejoDejetosLoteResponse {

    private Long id;
    private Long loteId;
    private String categoriaAnimal;
    private String tipoManejo;
    private BigDecimal percentualRebanho;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}