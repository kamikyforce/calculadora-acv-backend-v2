package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "manejo_dejetos_lote")
public class ManejoDejetosLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(name = "categoria_animal", nullable = false)
    private String categoriaAnimal;

    @Column(name = "tipo_manejo", nullable = false)
    private String tipoManejo;

    @Column(name = "percentual_rebanho", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualRebanho = BigDecimal.ZERO;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", insertable = false, updatable = false)
    private LoteRebanho lote;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

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

    public LoteRebanho getLote() {
        return lote;
    }

    public void setLote(LoteRebanho lote) {
        this.lote = lote;
    }
}