package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nutricao_animal_lote")
public class NutricaoAnimalLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(name = "inserir_dados_dieta", nullable = false)
    private Boolean inserirDadosDieta = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "sistema_producao")
    private SistemaProducao sistemaProducao;

    @Column(name = "tempo_pasto_horas_dia", precision = 5, scale = 2)
    private BigDecimal tempoPastoHorasDia = BigDecimal.ZERO;

    @Column(name = "tempo_pasto_dias_ano")
    private Integer tempoPastoDiasAno = 0;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", insertable = false, updatable = false)
    private LoteRebanho lote;

    @OneToMany(mappedBy = "nutricaoLote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IngredienteDietaLote> ingredientes;

    @OneToMany(mappedBy = "nutricaoLote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConcentradoDietaLote> concentrados;

    @OneToMany(mappedBy = "nutricaoLote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AditivoDietaLote> aditivos;

    public enum SistemaProducao {
        PASTO, SEMI_CONFINADO, CONFINADO
    }

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

    public Boolean getInserirDadosDieta() {
        return inserirDadosDieta;
    }

    public void setInserirDadosDieta(Boolean inserirDadosDieta) {
        this.inserirDadosDieta = inserirDadosDieta;
    }

    public SistemaProducao getSistemaProducao() {
        return sistemaProducao;
    }

    public void setSistemaProducao(SistemaProducao sistemaProducao) {
        this.sistemaProducao = sistemaProducao;
    }

    public BigDecimal getTempoPastoHorasDia() {
        return tempoPastoHorasDia;
    }

    public void setTempoPastoHorasDia(BigDecimal tempoPastoHorasDia) {
        this.tempoPastoHorasDia = tempoPastoHorasDia;
    }

    public Integer getTempoPastoDiasAno() {
        return tempoPastoDiasAno;
    }

    public void setTempoPastoDiasAno(Integer tempoPastoDiasAno) {
        this.tempoPastoDiasAno = tempoPastoDiasAno;
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

    public List<IngredienteDietaLote> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteDietaLote> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<ConcentradoDietaLote> getConcentrados() {
        return concentrados;
    }

    public void setConcentrados(List<ConcentradoDietaLote> concentrados) {
        this.concentrados = concentrados;
    }

    public List<AditivoDietaLote> getAditivos() {
        return aditivos;
    }

    public void setAditivos(List<AditivoDietaLote> aditivos) {
        this.aditivos = aditivos;
    }
}