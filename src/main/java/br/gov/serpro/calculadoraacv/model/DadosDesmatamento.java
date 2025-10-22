package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.*;
import br.gov.serpro.calculadoraacv.config.SiglaFitofisionomiaConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "dados_desmatamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosDesmatamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fator_mut_id")
    private FatorMut fatorMut;

    @Enumerated(EnumType.STRING)
    @Column(name = "bioma", nullable = false)
    private Bioma bioma;

    @Column(name = "valor_unico", nullable = false)
    private Boolean valorUnico;

    @ElementCollection(targetClass = UF.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "desmatamento_ufs", joinColumns = @JoinColumn(name = "dados_desmatamento_id"))
    @Column(name = "uf")
    private Set<UF> ufs;

    @Column(name = "nome_fitofisionomia", nullable = false)
    private String nomeFitofisionomia;

    @Convert(converter = SiglaFitofisionomiaConverter.class)
    @Column(name = "sigla_fitofisionomia", nullable = false)
    private SiglaFitofisionomia siglaFitofisionomia;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_desmatamento", nullable = false)
    private CategoriaDesmatamento categoriaDesmatamento;

    @Column(name = "estoque_carbono", precision = 15, scale = 6)
    private BigDecimal estoqueCarbono;

    @Column(name = "fator_co2", precision = 15, scale = 6)
    private BigDecimal fatorCO2;

    @Column(name = "fator_ch4", precision = 15, scale = 6)
    private BigDecimal fatorCH4;

    @Column(name = "fator_n2o", precision = 15, scale = 6)
    private BigDecimal fatorN2O;

    @Column(name = "replicado_automatico", nullable = false)
    private Boolean replicadoAutomatico = Boolean.FALSE;
}