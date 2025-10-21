package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fator_mut")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FatorMut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mudanca", nullable = false)
    private TipoMudanca tipoMudanca;

    @Enumerated(EnumType.STRING)
    @Column(name = "escopo", nullable = false)
    private EscopoEnum escopo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Dados específicos para Desmatamento
    @OneToMany(mappedBy = "fatorMut", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DadosDesmatamento> dadosDesmatamento;

    // Dados específicos para Vegetação
    @OneToMany(mappedBy = "fatorMut", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DadosVegetacao> dadosVegetacao;

    // Dados específicos para Solo
    @OneToMany(mappedBy = "fatorMut", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DadosSolo> dadosSolo;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}