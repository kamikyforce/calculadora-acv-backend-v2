package br.gov.serpro.calculadoraacv.dto;

public class CategoriaLoteResponse {
    private Long id;
    private Long loteId;
    private Integer quantidadeAnimais;
    private java.math.BigDecimal pesoMedio;
    private Long categoriaCorteId;
    private Long categoriaLeiteId;
    private String observacoes;
    private Integer animaisComprados;
    private java.math.BigDecimal pesoMedioComprados;
    private Integer animaisVendidos;
    private java.math.BigDecimal pesoMedioVendidos;
    private java.math.BigDecimal permanenciaMeses;
    private java.math.BigDecimal idadeDesmame;
    private java.math.BigDecimal femeasPrenhasPercentual;
    private java.math.BigDecimal producaoLeiteAno;
    private java.math.BigDecimal teorGorduraLeite;
    private java.math.BigDecimal teorProteinaLeite;
    private java.time.LocalDateTime dataCriacao;
    private java.time.LocalDateTime dataAtualizacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLoteId() { return loteId; }
    public void setLoteId(Long loteId) { this.loteId = loteId; }
    public Integer getQuantidadeAnimais() { return quantidadeAnimais; }
    public void setQuantidadeAnimais(Integer quantidadeAnimais) { this.quantidadeAnimais = quantidadeAnimais; }
    public java.math.BigDecimal getPesoMedio() { return pesoMedio; }
    public void setPesoMedio(java.math.BigDecimal pesoMedio) { this.pesoMedio = pesoMedio; }
    public Long getCategoriaCorteId() { return categoriaCorteId; }
    public void setCategoriaCorteId(Long categoriaCorteId) { this.categoriaCorteId = categoriaCorteId; }
    public Long getCategoriaLeiteId() { return categoriaLeiteId; }
    public void setCategoriaLeiteId(Long categoriaLeiteId) { this.categoriaLeiteId = categoriaLeiteId; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Integer getAnimaisComprados() { return animaisComprados; }
    public void setAnimaisComprados(Integer animaisComprados) { this.animaisComprados = animaisComprados; }
    public java.math.BigDecimal getPesoMedioComprados() { return pesoMedioComprados; }
    public void setPesoMedioComprados(java.math.BigDecimal pesoMedioComprados) { this.pesoMedioComprados = pesoMedioComprados; }
    public Integer getAnimaisVendidos() { return animaisVendidos; }
    public void setAnimaisVendidos(Integer animaisVendidos) { this.animaisVendidos = animaisVendidos; }
    public java.math.BigDecimal getPesoMedioVendidos() { return pesoMedioVendidos; }
    public void setPesoMedioVendidos(java.math.BigDecimal pesoMedioVendidos) { this.pesoMedioVendidos = pesoMedioVendidos; }
    public java.math.BigDecimal getPermanenciaMeses() { return permanenciaMeses; }
    public void setPermanenciaMeses(java.math.BigDecimal permanenciaMeses) { this.permanenciaMeses = permanenciaMeses; }
    public java.math.BigDecimal getIdadeDesmame() { return idadeDesmame; }
    public void setIdadeDesmame(java.math.BigDecimal idadeDesmame) { this.idadeDesmame = idadeDesmame; }
    public java.math.BigDecimal getFemeasPrenhasPercentual() { return femeasPrenhasPercentual; }
    public void setFemeasPrenhasPercentual(java.math.BigDecimal femeasPrenhasPercentual) { this.femeasPrenhasPercentual = femeasPrenhasPercentual; }
    public java.math.BigDecimal getProducaoLeiteAno() { return producaoLeiteAno; }
    public void setProducaoLeiteAno(java.math.BigDecimal producaoLeiteAno) { this.producaoLeiteAno = producaoLeiteAno; }
    public java.math.BigDecimal getTeorGorduraLeite() { return teorGorduraLeite; }
    public void setTeorGorduraLeite(java.math.BigDecimal teorGorduraLeite) { this.teorGorduraLeite = teorGorduraLeite; }
    public java.math.BigDecimal getTeorProteinaLeite() { return teorProteinaLeite; }
    public void setTeorProteinaLeite(java.math.BigDecimal teorProteinaLeite) { this.teorProteinaLeite = teorProteinaLeite; }
    public java.time.LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(java.time.LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public java.time.LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(java.time.LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}