package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bd_dieta_ingredientes")
@Data
@EqualsAndHashCode(callSuper = false)
public class BdDietaIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_ingrediente", length = 10, nullable = false)
    private String tipoIngrediente;

    @Column(name = "nome_ingrediente", length = 100, nullable = false)
    private String nomeIngrediente;

    @Column(name = "ndt_percentual", precision = 6, scale = 2)
    private BigDecimal ndtPercentual;

    @Column(name = "energia_bruta", precision = 6, scale = 2)
    private BigDecimal energiaBruta;

    @Column(name = "materia_seca", precision = 8, scale = 2)
    private BigDecimal materiaSeca;

    @Column(name = "proteina_bruta_ms", precision = 8, scale = 2)
    private BigDecimal proteinaBrutaMs;

    @Column(name = "fibra_detergente_neutro", precision = 6, scale = 2)
    private BigDecimal fibraDetrgenteNeutro;

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