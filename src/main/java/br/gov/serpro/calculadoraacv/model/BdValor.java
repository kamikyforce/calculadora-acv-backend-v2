package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bd_valor")
@Data
@EqualsAndHashCode(callSuper = false)
public class BdValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingrediente_padronizacao", length = 100)
    private String ingredientePadronizacao;

    @Column(name = "nome_alimento", length = 100)
    private String nomeAlimento;

    @Column(length = 10)
    private String tipo;

    @Column(name = "representatividade_corte", length = 20)
    private String representatividadeCorte;

    @Column(name = "representatividade_leite", length = 20)
    private String representatividadeLeite;

    @Column(name = "materia_seca", precision = 8, scale = 2)
    private BigDecimal materiaSeca;

    @Column(name = "proteina_bruta", precision = 8, scale = 2)
    private BigDecimal proteinaBruta;

    @Column(name = "fibra_bruta", precision = 8, scale = 2)
    private BigDecimal fibraBruta;

    @Column(name = "fibra_detergente_neutro", precision = 8, scale = 2)
    private BigDecimal fibraDetrgenteNeutro;

    @Column(name = "fibra_detergente_acido", precision = 8, scale = 2)
    private BigDecimal fibraDetrgenteAcido;

    @Column(name = "extrato_etereo", precision = 8, scale = 2)
    private BigDecimal extratoEtereo;

    @Column(name = "materia_mineral", precision = 8, scale = 2)
    private BigDecimal materiaMineral;

    @Column(precision = 8, scale = 2)
    private BigDecimal ndt;

    @Column(name = "ndt_calculado", precision = 8, scale = 2)
    private BigDecimal ndtCalculado;

    @Column(name = "energia_bruta_total", precision = 8, scale = 2)
    private BigDecimal energiaBrutaTotal;

    @Column(name = "energia_digestivel", precision = 8, scale = 2)
    private BigDecimal energiaDigestivel;

    @Column(name = "degradabilidade_energia_bruta", precision = 8, scale = 2)
    private BigDecimal degradabilidadeEnergiaBruta;

    @Column(name = "degradabilidade_ms", precision = 8, scale = 2)
    private BigDecimal degradabilidadeMs;

    @Column(name = "degradabilidade_mo", precision = 8, scale = 2)
    private BigDecimal degradabilidadeMo;

    @Column(name = "edigestivel_ebruta", length = 20)
    private String edigestivelEbruta;

    @Column(name = "energia_bruta_mj", precision = 8, scale = 2)
    private BigDecimal energiaBrutaMj;

    @Column(name = "ms_g_kg", precision = 8, scale = 1)
    private BigDecimal msGKg;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

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