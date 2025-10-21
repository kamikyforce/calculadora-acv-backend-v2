package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.gov.serpro.calculadoraacv.model.OrigemProducao;

@Entity
@Table(name = "aditivo_dieta_lote")
public class AditivoDietaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nutricao_lote_id", nullable = false)
    private Long nutricaoLoteId;

    @Column(name = "nome_aditivo", nullable = false)
    private String nomeAditivo;

    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual = BigDecimal.ZERO;

    @Column(name = "tipo", length = 255)
    private String tipo;

    @Column(name = "dose", precision = 10, scale = 3)
    private BigDecimal dose;

    @Column(name = "oferta", precision = 10, scale = 3)
    private BigDecimal oferta;

    @Column(name = "percentual_adicional", precision = 5, scale = 2)
    private BigDecimal percentualAdicional;

    @Column(name = "quantidade_kg_cab_dia", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidadeKgCabDia = BigDecimal.ZERO;

    @Column(name = "oferta_dias_ano", nullable = false)
    private Integer ofertaDiasAno = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "producao", nullable = false)
    private OrigemProducao producao = OrigemProducao.INTERNA;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutricao_lote_id", insertable = false, updatable = false)
    private NutricaoAnimalLote nutricaoLote;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public NutricaoAnimalLote getNutricaoLote() {
        return nutricaoLote;
    }

    public void setNutricaoLote(NutricaoAnimalLote nutricaoLote) {
        this.nutricaoLote = nutricaoLote;
    }
}