package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categoria_lote")
public class CategoriaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(name = "categoria_corte_id")
    private Long categoriaCorteId;

    @Column(name = "categoria_leite_id")
    private Long categoriaLeiteId;

    @Column(name = "animais_fazenda", nullable = false)
    private Integer animaisFazenda = 0;

    @Column(name = "peso_medio_vivo", nullable = false, precision = 10, scale = 6)
    private BigDecimal pesoMedioVivo = BigDecimal.ZERO;

    @Column(name = "animais_comprados")
    private Integer animaisComprados = 0;

    @Column(name = "peso_medio_comprados", precision = 10, scale = 6)
    private BigDecimal pesoMedioComprados = BigDecimal.ZERO;

    @Column(name = "animais_vendidos")
    private Integer animaisVendidos = 0;

    @Column(name = "peso_medio_vendidos", precision = 10, scale = 6)
    private BigDecimal pesoMedioVendidos = BigDecimal.ZERO;

    @Column(name = "permanencia_meses", precision = 10, scale = 6)
    private BigDecimal permanenciaMeses = BigDecimal.ZERO;

    @Column(name = "idade_desmame", precision = 10, scale = 6)
    private BigDecimal idadeDesmame = BigDecimal.ZERO;

    @Column(name = "femeas_prenhas_percentual", precision = 5, scale = 2)
    private BigDecimal femeasPrenhasPercentual = BigDecimal.ZERO;

    @Column(name = "producao_leite_ano", precision = 10, scale = 2)
    private BigDecimal producaoLeiteAno = BigDecimal.ZERO;

    @Column(name = "teor_gordura_leite", precision = 5, scale = 2)
    private BigDecimal teorGorduraLeite = BigDecimal.ZERO;

    @Column(name = "teor_proteina_leite", precision = 5, scale = 2)
    private BigDecimal teorProteinaLeite = BigDecimal.ZERO;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", insertable = false, updatable = false)
    private LoteRebanho lote;

    @OneToMany(mappedBy = "categoriaLote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarCategoria> cars;

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

    public Long getCategoriaCorteId() {
        return categoriaCorteId;
    }

    public void setCategoriaCorteId(Long categoriaCorteId) {
        this.categoriaCorteId = categoriaCorteId;
    }

    public Long getCategoriaLeiteId() {
        return categoriaLeiteId;
    }

    public void setCategoriaLeiteId(Long categoriaLeiteId) {
        this.categoriaLeiteId = categoriaLeiteId;
    }

    public Integer getAnimaisFazenda() {
        return animaisFazenda;
    }

    public void setAnimaisFazenda(Integer animaisFazenda) {
        this.animaisFazenda = animaisFazenda;
    }

    public BigDecimal getPesoMedioVivo() {
        return pesoMedioVivo;
    }

    public void setPesoMedioVivo(BigDecimal pesoMedioVivo) {
        this.pesoMedioVivo = pesoMedioVivo;
    }

    public Integer getAnimaisComprados() {
        return animaisComprados;
    }

    public void setAnimaisComprados(Integer animaisComprados) {
        this.animaisComprados = animaisComprados;
    }

    public BigDecimal getPesoMedioComprados() {
        return pesoMedioComprados;
    }

    public void setPesoMedioComprados(BigDecimal pesoMedioComprados) {
        this.pesoMedioComprados = pesoMedioComprados;
    }

    public Integer getAnimaisVendidos() {
        return animaisVendidos;
    }

    public void setAnimaisVendidos(Integer animaisVendidos) {
        this.animaisVendidos = animaisVendidos;
    }

    public BigDecimal getPesoMedioVendidos() {
        return pesoMedioVendidos;
    }

    public void setPesoMedioVendidos(BigDecimal pesoMedioVendidos) {
        this.pesoMedioVendidos = pesoMedioVendidos;
    }

    public BigDecimal getPermanenciaMeses() {
        return permanenciaMeses;
    }

    public void setPermanenciaMeses(BigDecimal permanenciaMeses) {
        this.permanenciaMeses = permanenciaMeses;
    }

    public BigDecimal getIdadeDesmame() {
        return idadeDesmame;
    }

    public void setIdadeDesmame(BigDecimal idadeDesmame) {
        this.idadeDesmame = idadeDesmame;
    }

    public BigDecimal getFemeasPrenhasPercentual() {
        return femeasPrenhasPercentual;
    }

    public void setFemeasPrenhasPercentual(BigDecimal femeasPrenhasPercentual) {
        this.femeasPrenhasPercentual = femeasPrenhasPercentual;
    }

    public BigDecimal getProducaoLeiteAno() {
        return producaoLeiteAno;
    }

    public void setProducaoLeiteAno(BigDecimal producaoLeiteAno) {
        this.producaoLeiteAno = producaoLeiteAno;
    }

    public BigDecimal getTeorGorduraLeite() {
        return teorGorduraLeite;
    }

    public void setTeorGorduraLeite(BigDecimal teorGorduraLeite) {
        this.teorGorduraLeite = teorGorduraLeite;
    }

    public BigDecimal getTeorProteinaLeite() {
        return teorProteinaLeite;
    }

    public void setTeorProteinaLeite(BigDecimal teorProteinaLeite) {
        this.teorProteinaLeite = teorProteinaLeite;
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

    public List<CarCategoria> getCars() {
        return cars;
    }

    public void setCars(List<CarCategoria> cars) {
        this.cars = cars;
    }
}