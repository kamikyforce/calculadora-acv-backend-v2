package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fatores_energia_eletrica")
public class FatorEnergiaEletrica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer ano;
    
    @Column(nullable = false)
    private Integer mes;
    
    @Column(name = "fator_emissao", precision = 10, scale = 6, nullable = false)
    private BigDecimal fatorEmissao;
    
    @Column(length = 50)
    private String unidade = "tCO2/MWh";
    
    @Column(length = 100)
    private String referencia = "MCT 2015";
    
    @Column(name = "percentual_etanol_gasolina", precision = 5, scale = 2)
    private BigDecimal percentualEtanolGasolina;
    
    @Column(name = "percentual_biodiesel_diesel", precision = 5, scale = 2)
    private BigDecimal percentualBiodieselDiesel;
    
    @Column(name = "fator_medio_anual", precision = 10, scale = 6)
    private BigDecimal fatorMedioAnual;
    
    @Column(name = "tipo_dado", length = 20)
    private String tipoDado;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public FatorEnergiaEletrica() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public FatorEnergiaEletrica(Integer ano, Integer mes, BigDecimal fatorEmissao) {
        this();
        this.ano = ano;
        this.mes = mes;
        this.fatorEmissao = fatorEmissao;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    
    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }
    
    public BigDecimal getFatorEmissao() { return fatorEmissao; }
    public void setFatorEmissao(BigDecimal fatorEmissao) { this.fatorEmissao = fatorEmissao; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    
    public BigDecimal getPercentualEtanolGasolina() { return percentualEtanolGasolina; }
    public void setPercentualEtanolGasolina(BigDecimal percentualEtanolGasolina) { this.percentualEtanolGasolina = percentualEtanolGasolina; }
    
    public BigDecimal getPercentualBiodieselDiesel() { return percentualBiodieselDiesel; }
    public void setPercentualBiodieselDiesel(BigDecimal percentualBiodieselDiesel) { this.percentualBiodieselDiesel = percentualBiodieselDiesel; }
    
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    
    public String getTipoDado() { return tipoDado; }
    public void setTipoDado(String tipoDado) { this.tipoDado = tipoDado; }
    
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