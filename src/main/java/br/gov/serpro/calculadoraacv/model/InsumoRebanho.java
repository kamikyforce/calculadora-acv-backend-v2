package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insumos_rebanho")
public class InsumoRebanho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false, length = 50)
    private String modulo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInsumo tipo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EscopoEnum escopo;
    
    @Column(name = "identificacao_produto", nullable = false)
    private String identificacaoProduto;
    
    @Column(name = "fonte_dataset")
    private String fonteDataset;
    
    @Column(name = "dataset_produto")
    private String datasetProduto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_produto")
    private UnidadeProdutoReferencia unidadeProduto;
    
    @Column(name = "metodo_avaliacao_gwp", length = 100)
    private String metodoAvaliacaoGwp;
    
    @Column(name = "gee_total", precision = 15, scale = 6)
    private BigDecimal geeTotal;
    
    @Column(name = "co2_fossil", precision = 15, scale = 6)
    private BigDecimal co2Fossil;
    
    @Column(name = "uso_terra", precision = 15, scale = 6)
    private BigDecimal usoTerra;
    
    @Column(name = "ch4_fossil", precision = 15, scale = 6)
    private BigDecimal ch4Fossil;
    
    @Column(name = "ch4_biogenico", precision = 15, scale = 6)
    private BigDecimal ch4Biogenico;
    
    @Column(precision = 15, scale = 6)
    private BigDecimal n2o;
    
    @Column(name = "outras_substancias", precision = 15, scale = 6)
    private BigDecimal outrasSubstancias;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_ingrediente")
    private GrupoIngredienteAlimentar grupoIngrediente;
    
    @Column(name = "nome_produto")
    private String nomeProduto;
    
    @Column(length = 100)
    private String uuid;
    
    @Column(precision = 15, scale = 6)
    private BigDecimal quantidade;

    @Column(name = "quantidade_produto_referencia", precision = 15, scale = 6)
    private BigDecimal quantidadeProdutoReferencia;

    @Column(length = 20)
    private String unidade;

    @Column(name = "gwp_100_fossil", precision = 15, scale = 6)
    private BigDecimal gwp100Fossil;
    
    @Column(name = "gwp_100_biogenico", precision = 15, scale = 6)
    private BigDecimal gwp100Biogenico;
    
    @Column(name = "gwp_100_transformacao", precision = 15, scale = 6)
    private BigDecimal gwp100Transformacao;
    
    @Column(name = "co2_ch4_transformacao", precision = 15, scale = 6)
    private BigDecimal co2Ch4Transformacao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "faz_parte_dieta")
    private FazParteDieta fazParteDieta;
    
    private String ingrediente;
    
    @Column(name = "not_eu", length = 100)
    private String notEu;
    
    @Column(name = "energia_bruta", precision = 10, scale = 2)
    private BigDecimal energiaBruta;
    
    @Column(name = "ms", precision = 10, scale = 2)
    private BigDecimal ms;
    
    @Column(name = "proteina_bruta", precision = 10, scale = 2)
    private BigDecimal proteinaBruta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "fatores_emissao")
    private FatoresEmissao fatoresEmissao;
    
    @Column(columnDefinition = "TEXT")
    private String comentarios;
    
    @Column(name = "comentarios_escopo1", columnDefinition = "TEXT")
    private String comentariosEscopo1;
    
    @Column(name = "comentarios_escopo3", columnDefinition = "TEXT")
    private String comentariosEscopo3;
    
    @Column(name = "gee_total_escopo1", precision = 15, scale = 6)
    private BigDecimal geeTotalEscopo1;
    
    @Column(name = "co2_fossil_escopo1", precision = 15, scale = 6)
    private BigDecimal co2FossilEscopo1;
    
    @Column(name = "uso_terra_escopo1", precision = 15, scale = 6)
    private BigDecimal usoTerraEscopo1;
    
    @Column(name = "ch4_fossil_escopo1", precision = 15, scale = 6)
    private BigDecimal ch4FossilEscopo1;
    
    @Column(name = "ch4_biogenico_escopo1", precision = 15, scale = 6)
    private BigDecimal ch4BiogenicoEscopo1;
    
    @Column(name = "n2o_escopo1", precision = 15, scale = 6)
    private BigDecimal n2oEscopo1;
    
    @Column(name = "outras_substancias_escopo1", precision = 15, scale = 6)
    private BigDecimal outrasSubstanciasEscopo1;
    
    @Column(name = "gee_total_escopo3", precision = 15, scale = 6)
    private BigDecimal geeTotalEscopo3;
    
    @Column(name = "gwp_100_fossil_escopo3", precision = 15, scale = 6)
    private BigDecimal gwp100FossilEscopo3;
    
    @Column(name = "gwp_100_biogenico_escopo3", precision = 15, scale = 6)
    private BigDecimal gwp100BiogenicoEscopo3;
    
    @Column(name = "gwp_100_transformacao_escopo3", precision = 15, scale = 6)
    private BigDecimal gwp100TransformacaoEscopo3;
    
    @Column(name = "dioxido_carbono_fossil_escopo3", precision = 15, scale = 6)
    private BigDecimal dioxidoCarbonoFossilEscopo3;
    
    @Column(name = "dioxido_carbono_metano_transformacao_escopo3", precision = 15, scale = 6)
    private BigDecimal dioxidoCarbonoMetanoTransformacaoEscopo3;
    
    @Column(name = "metano_fossil_escopo3", precision = 15, scale = 6)
    private BigDecimal metanoFossilEscopo3;
    
    @Column(name = "metano_biogenico_escopo3", precision = 15, scale = 6)
    private BigDecimal metanoBiogenicoEscopo3;
    
    @Column(name = "oxido_nitroso_escopo3", precision = 15, scale = 6)
    private BigDecimal oxidoNitrosoEscopo3;
    
    @Column(name = "outras_substancias_escopo3", precision = 15, scale = 6)
    private BigDecimal outrasSubstanciasEscopo3;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (dataAtualizacao == null) {
            dataAtualizacao = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}