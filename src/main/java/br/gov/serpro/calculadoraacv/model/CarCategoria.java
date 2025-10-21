package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_categoria")
public class CarCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria_lote_id", nullable = false)
    private Long categoriaLoteId;

    @Column(name = "numero_car", nullable = false)
    private String numeroCar;

    @Column(name = "animais_comprados", nullable = false)
    private Integer animaisComprados = 0;

    @Column(name = "ordem", nullable = false)
    private Integer ordem = 1;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_lote_id", insertable = false, updatable = false)
    private CategoriaLote categoriaLote;

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

    public Long getCategoriaLoteId() {
        return categoriaLoteId;
    }

    public void setCategoriaLoteId(Long categoriaLoteId) {
        this.categoriaLoteId = categoriaLoteId;
    }

    public String getNumeroCar() {
        return numeroCar;
    }

    public void setNumeroCar(String numeroCar) {
        this.numeroCar = numeroCar;
    }

    public Integer getAnimaisComprados() {
        return animaisComprados;
    }

    public void setAnimaisComprados(Integer animaisComprados) {
        this.animaisComprados = animaisComprados;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public CategoriaLote getCategoriaLote() {
        return categoriaLote;
    }

    public void setCategoriaLote(CategoriaLote categoriaLote) {
        this.categoriaLote = categoriaLote;
    }
}