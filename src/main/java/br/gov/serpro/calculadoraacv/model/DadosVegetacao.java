package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "dados_vegetacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosVegetacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fator_mut_id")
    private FatorMut fatorMut;

    @ElementCollection(targetClass = CategoriaDesmatamento.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "vegetacao_categorias", joinColumns = @JoinColumn(name = "dados_vegetacao_id"))
    @Column(name = "categoria")
    private Set<CategoriaDesmatamento> categoriasFitofisionomia;

    @Column(name = "parametro", length = 500)
    private String parametro;

    @Column(name = "valor_amazonia", precision = 15, scale = 6)
    private BigDecimal valorAmazonia;

    @Column(name = "valor_caatinga", precision = 15, scale = 6)
    private BigDecimal valorCaatinga;

    @Column(name = "valor_cerrado", precision = 15, scale = 6)
    private BigDecimal valorCerrado;

    @Column(name = "valor_mata_atlantica", precision = 15, scale = 6)
    private BigDecimal valorMataAtlantica;

    @Column(name = "valor_pampa", precision = 15, scale = 6)
    private BigDecimal valorPampa;

    @Column(name = "valor_pantanal", precision = 15, scale = 6)
    private BigDecimal valorPantanal;

    @Enumerated(EnumType.STRING)
    @Column(name = "bioma")
    private Bioma bioma;

    @Column(name = "fator_co2", precision = 15, scale = 6)
    private BigDecimal fatorCO2;

    @Column(name = "fator_ch4", precision = 15, scale = 6)
    private BigDecimal fatorCH4;

    @Column(name = "fator_n2o", precision = 15, scale = 6)
    private BigDecimal fatorN2O;

    // ✅ NOVOS CAMPOS ADICIONADOS
    @Column(name = "especie_vegetacao", length = 255)
    private String especieVegetacao;

    @Column(name = "altura_media", precision = 10, scale = 2)
    private BigDecimal alturaMedia;

    @Column(name = "biomassa_aerea", precision = 15, scale = 6)
    private BigDecimal biomassaAerea;

    @Column(name = "biomassa_subterranea", precision = 15, scale = 6)
    private BigDecimal biomassaSubterranea;
}