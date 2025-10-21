package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventario_jornada")
public class InventarioJornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rebanho", nullable = false)
    private TipoRebanho tipoRebanho;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusInventario status = StatusInventario.RASCUNHO;

    @Column(name = "fase_atual", nullable = false)
    private Integer faseAtual = 1;

    @Column(name = "fase_rebanho_concluida", nullable = false)
    private Boolean faseRebanhoConcluida = false;

    @Column(name = "fase_producao_agricola_concluida", nullable = false)
    private Boolean faseProducaoAgricolaConcluida = false;

    @Column(name = "fase_mut_concluida", nullable = false)
    private Boolean faseMutConcluida = false;

    @Column(name = "fase_energia_concluida", nullable = false)
    private Boolean faseEnergiaConcluida = false;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoteRebanho> lotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;

    public enum TipoRebanho {
        CORTE, LEITE
    }

    public enum StatusInventario {
        RASCUNHO, EM_ANDAMENTO, CONCLUIDO
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoRebanho getTipoRebanho() {
        return tipoRebanho;
    }

    public void setTipoRebanho(TipoRebanho tipoRebanho) {
        this.tipoRebanho = tipoRebanho;
    }

    public StatusInventario getStatus() {
        return status;
    }

    public void setStatus(StatusInventario status) {
        this.status = status;
    }

    public Integer getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(Integer faseAtual) {
        this.faseAtual = faseAtual;
    }

    public Boolean getFaseRebanhoConcluida() {
        return faseRebanhoConcluida;
    }

    public void setFaseRebanhoConcluida(Boolean faseRebanhoConcluida) {
        this.faseRebanhoConcluida = faseRebanhoConcluida;
    }

    public Boolean getFaseProducaoAgricolaConcluida() {
        return faseProducaoAgricolaConcluida;
    }

    public void setFaseProducaoAgricolaConcluida(Boolean faseProducaoAgricolaConcluida) {
        this.faseProducaoAgricolaConcluida = faseProducaoAgricolaConcluida;
    }

    public Boolean getFaseMutConcluida() {
        return faseMutConcluida;
    }

    public void setFaseMutConcluida(Boolean faseMutConcluida) {
        this.faseMutConcluida = faseMutConcluida;
    }

    public Boolean getFaseEnergiaConcluida() {
        return faseEnergiaConcluida;
    }

    public void setFaseEnergiaConcluida(Boolean faseEnergiaConcluida) {
        this.faseEnergiaConcluida = faseEnergiaConcluida;
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

    public List<LoteRebanho> getLotes() {
        return lotes;
    }

    public void setLotes(List<LoteRebanho> lotes) {
        this.lotes = lotes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}