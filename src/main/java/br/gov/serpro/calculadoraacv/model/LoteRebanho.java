package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lote_rebanho")
public class LoteRebanho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventario_id", nullable = false)
    private Long inventarioId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ordem", nullable = false)
    private Integer ordem = 1;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id", insertable = false, updatable = false)
    private InventarioJornada inventario;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoriaLote> categorias;

    @OneToOne(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NutricaoAnimalLote nutricaoAnimal;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManejoDejetosLote> manejosDejetos;

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

    public Long getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public InventarioJornada getInventario() {
        return inventario;
    }

    public void setInventario(InventarioJornada inventario) {
        this.inventario = inventario;
    }

    public List<CategoriaLote> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaLote> categorias) {
        this.categorias = categorias;
    }

    public NutricaoAnimalLote getNutricaoAnimal() {
        return nutricaoAnimal;
    }

    public void setNutricaoAnimal(NutricaoAnimalLote nutricaoAnimal) {
        this.nutricaoAnimal = nutricaoAnimal;
    }

    public List<ManejoDejetosLote> getManejosDejetos() {
        return manejosDejetos;
    }

    public void setManejosDejetos(List<ManejoDejetosLote> manejosDejetos) {
        this.manejosDejetos = manejosDejetos;
    }
}