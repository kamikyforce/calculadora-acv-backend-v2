package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.gov.serpro.calculadoraacv.model.OrigemProducao;

@Entity
@Table(name = "concentrado_dieta_lote")
public class ConcentradoDietaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nutricao_lote_id", nullable = false)
    private Long nutricaoLoteId;

    @Column(name = "nome_concentrado", nullable = false)
    private String nomeConcentrado;

    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual = BigDecimal.ZERO;

    @Column(name = "proteina_bruta_percentual", precision = 5, scale = 2)
    private BigDecimal proteinaBrutaPercentual;

    @Column(name = "ureia", length = 50)
    private String ureia;

    @Column(name = "subproduto", length = 255)
    private String subproduto;

    @Column(name = "quantidade", precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "oferta", precision = 10, scale = 3)
    private BigDecimal oferta;

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