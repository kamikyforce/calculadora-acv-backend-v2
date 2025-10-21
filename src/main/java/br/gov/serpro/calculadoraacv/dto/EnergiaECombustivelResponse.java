package br.gov.serpro.calculadoraacv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnergiaECombustivelResponse {
    private Long id;
    private Long usuarioId;
    private String tipoEnergia;
    private String fonteEnergia;
    private BigDecimal consumoAnual;
    private String unidade;
    private BigDecimal fatorEmissao;
    private BigDecimal fatorMedioAnual; // ← ADICIONAR ESTE CAMPO
    private String escopo;
    private Integer anoReferencia;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    // NOVO CAMPO PARA DADOS MENSAIS
    private String dadosMensais;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getTipoEnergia() { return tipoEnergia; }
    public void setTipoEnergia(String tipoEnergia) { this.tipoEnergia = tipoEnergia; }
    
    public String getFonteEnergia() { return fonteEnergia; }
    public void setFonteEnergia(String fonteEnergia) { this.fonteEnergia = fonteEnergia; }
    
    public BigDecimal getConsumoAnual() { return consumoAnual; }
    public void setConsumoAnual(BigDecimal consumoAnual) { this.consumoAnual = consumoAnual; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    public BigDecimal getFatorEmissao() { return fatorEmissao; }
    public void setFatorEmissao(BigDecimal fatorEmissao) { this.fatorEmissao = fatorEmissao; }
    
    // ADICIONAR GETTER E SETTER
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    // NOVO GETTER E SETTER PARA ESCOPO
    public String getEscopo() { return escopo; }
    public void setEscopo(String escopo) { this.escopo = escopo; }
    
    // GETTER E SETTER PARA ANO DE REFERÊNCIA
    public Integer getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(Integer anoReferencia) { this.anoReferencia = anoReferencia; }
    
    // GETTER E SETTER PARA DADOS MENSAIS
    public String getDadosMensais() { return dadosMensais; }
    public void setDadosMensais(String dadosMensais) { this.dadosMensais = dadosMensais; }
}