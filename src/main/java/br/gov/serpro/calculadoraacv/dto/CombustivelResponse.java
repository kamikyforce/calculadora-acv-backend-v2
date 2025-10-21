package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CombustivelResponse {
    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal fatorEmissaoCO2;
    private BigDecimal fatorEmissaoCH4;
    private BigDecimal fatorEmissaoN2O;
    private String unidade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // NEW FIELD FOR SCOPE SUPPORT
    private EscopoEnum escopo;
    private Long usuarioId;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getFatorEmissaoCO2() { return fatorEmissaoCO2; }
    public void setFatorEmissaoCO2(BigDecimal fatorEmissaoCO2) { this.fatorEmissaoCO2 = fatorEmissaoCO2; }
    
    public BigDecimal getFatorEmissaoCH4() { return fatorEmissaoCH4; }
    public void setFatorEmissaoCH4(BigDecimal fatorEmissaoCH4) { this.fatorEmissaoCH4 = fatorEmissaoCH4; }
    
    public BigDecimal getFatorEmissaoN2O() { return fatorEmissaoN2O; }
    public void setFatorEmissaoN2O(BigDecimal fatorEmissaoN2O) { this.fatorEmissaoN2O = fatorEmissaoN2O; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    // NEW GETTERS/SETTERS FOR SCOPE SUPPORT
    public EscopoEnum getEscopo() { return escopo; }
    public void setEscopo(EscopoEnum escopo) { this.escopo = escopo; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}