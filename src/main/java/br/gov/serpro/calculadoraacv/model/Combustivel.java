package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "combustiveis")
public class Combustivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(name = "fator_emissao_co2", precision = 10, scale = 6)
    private BigDecimal fatorEmissaoCO2;
    
    @Column(name = "fator_emissao_ch4", precision = 10, scale = 6)
    private BigDecimal fatorEmissaoCH4;
    
    @Column(name = "fator_emissao_n2o", precision = 10, scale = 6)
    private BigDecimal fatorEmissaoN2O;
    
    @Column(length = 20)
    private String unidade;
    
    // NEW FIELDS FOR SCOPE SUPPORT
    @Enumerated(EnumType.STRING)
    @Column(name = "escopo")
    private EscopoEnum escopo;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    // Construtores
    public Combustivel() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public Combustivel(String nome, String tipo, BigDecimal fatorEmissaoCO2, 
                      BigDecimal fatorEmissaoCH4, BigDecimal fatorEmissaoN2O, String unidade) {
        this();
        this.nome = nome;
        this.tipo = tipo;
        this.fatorEmissaoCO2 = fatorEmissaoCO2;
        this.fatorEmissaoCH4 = fatorEmissaoCH4;
        this.fatorEmissaoN2O = fatorEmissaoN2O;
        this.unidade = unidade;
    }
    
    // NEW CONSTRUCTOR WITH SCOPE SUPPORT
    public Combustivel(String nome, String tipo, BigDecimal fatorEmissaoCO2, 
                      BigDecimal fatorEmissaoCH4, BigDecimal fatorEmissaoN2O, 
                      String unidade, EscopoEnum escopo, Long usuarioId) {
        this(nome, tipo, fatorEmissaoCO2, fatorEmissaoCH4, fatorEmissaoN2O, unidade);
        this.escopo = escopo;
        this.usuarioId = usuarioId;
    }
    
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
    
    // NEW GETTERS/SETTERS FOR SCOPE SUPPORT
    public EscopoEnum getEscopo() { return escopo; }
    public void setEscopo(EscopoEnum escopo) { this.escopo = escopo; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}