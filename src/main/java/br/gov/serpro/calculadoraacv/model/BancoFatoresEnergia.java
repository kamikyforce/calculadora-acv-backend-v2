package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "banco_fatores_energia")
public class BancoFatoresEnergia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Integer ano;
    
    @Column(name = "fator_medio_anual", precision = 10, scale = 6, nullable = false)
    private BigDecimal fatorMedioAnual;
    
    @Column(length = 100)
    private String fonte = "SIN - Sistema Interligado Nacional";
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public BancoFatoresEnergia() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public BancoFatoresEnergia(Integer ano, BigDecimal fatorMedioAnual) {
        this();
        this.ano = ano;
        this.fatorMedioAnual = fatorMedioAnual;
    }
    
    public BancoFatoresEnergia(Integer ano, BigDecimal fatorMedioAnual, String observacoes) {
        this(ano, fatorMedioAnual);
        this.observacoes = observacoes;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    
    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}