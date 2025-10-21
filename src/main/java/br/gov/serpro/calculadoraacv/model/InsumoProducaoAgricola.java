package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insumos_producao_agricola")
public class InsumoProducaoAgricola {
    
    // Campos de controle
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    // ESCOPO 1 - Classificação
    @Column(length = 100)
    private String classe;
    
    @Column(length = 255)
    private String especificacao;
    
    // ESCOPO 1 - Teor de macronutrientes
    @Column(name = "teor_nitrogenio", precision = 5, scale = 2)
    private BigDecimal teorNitrogenio;
    
    @Column(name = "teor_fosforo", precision = 5, scale = 2)
    private BigDecimal teorFosforo;
    
    @Column(name = "teor_potassio", precision = 5, scale = 2)
    private BigDecimal teorPotassio;
    
    // ESCOPO 1 - Fator de conversão
    @Column(name = "fator_conversao", precision = 15, scale = 6)
    private BigDecimal fatorConversao;
    
    @Column(name = "unidade_fator_conversao", length = 50)
    private String fatorConversaoUnidade;
    
    // ESCOPO 1 - Quantidade e unidade de referência
    @Column(precision = 15, scale = 6)
    private BigDecimal quantidade;
    
    @Column(name = "unidade_referencia", length = 50)
    private String unidadeReferencia;
    
    // ESCOPO 1 - Fatores de emissão
    @Column(name = "fe_co2_biogenico", precision = 15, scale = 6)
    private BigDecimal feCo2Biogenico;
    
    @Column(name = "ref_fe_co2_biogenico", length = 500)
    private String refFeCo2Biogenico;
    
    @Column(name = "fe_co2", precision = 15, scale = 6)
    private BigDecimal feCo2;
    
    @Column(name = "ref_fe_co2", length = 500)
    private String refFeCo2;
    
    @Column(name = "fe_ch4", precision = 15, scale = 6)
    private BigDecimal feCh4;
    
    @Column(name = "ref_fe_ch4", length = 500)
    private String refFeCh4;
    
    @Column(name = "fe_n2o_direto", precision = 15, scale = 6)
    private BigDecimal feN2oDireto;
    
    @Column(name = "ref_fe_n2o_direto", length = 500)
    private String refFeN2oDireto;
    
    @Column(name = "frac_n2o_volatilizacao", precision = 15, scale = 6)
    private BigDecimal fracN2oVolatilizacao;
    
    @Column(name = "ref_frac_n2o_volatilizacao", length = 500)
    private String refFracN2oVolatilizacao;
    
    @Column(name = "frac_n2o_lixiviacao", precision = 15, scale = 6)
    private BigDecimal fracN2oLixiviacao;
    
    @Column(name = "ref_frac_n2o_lixiviacao", length = 500)
    private String refFracN2oLixiviacao;
    
    @Column(name = "fe_n2o_composto", precision = 15, scale = 6)
    private BigDecimal feN2oComposto;
    
    @Column(name = "ref_fe_n2o_composto", length = 500)
    private String refFeN2oComposto;
    
    @Column(name = "fe_co", precision = 15, scale = 6)
    private BigDecimal feCo;
    
    @Column(name = "ref_fe_co", length = 500)
    private String refFeCo;
    
    @Column(name = "fe_nox", precision = 15, scale = 6)
    private BigDecimal feNox;
    
    @Column(name = "ref_fe_nox", length = 500)
    private String refFeNox;
    
    // ESCOPO 3 - Identificação e classificação
    @Column(name = "grupo_ingrediente", length = 100)
    private String grupoIngrediente;
    
    @Column(name = "nome_produto", length = 200)
    private String nomeProduto;
    
    @Column(name = "tipo_produto", length = 100)
    private String tipoProduto;
    
    // ESCOPO 3 - Quantidade e unidade de referência
    @Column(name = "qtd_produto_referencia", precision = 15, scale = 6)
    private BigDecimal quantidadeProdutoReferencia;
    
    @Column(name = "unidade_produto_referencia", length = 50)
    private String unidadeProdutoReferencia;
    
    @Column(name = "unidade_produto", length = 50)
    private String unidadeProduto;
    
    // ESCOPO 3 - Quantidade e unidade
    @Column(name = "quantidade_produto", precision = 15, scale = 6)
    private BigDecimal quantidadeProduto;
    
    // ESCOPO 3 - Valores de emissões (GEE)
    @Column(name = "gee_total", precision = 15, scale = 6)
    private BigDecimal geeTotal;
    
    @Column(name = "gwp_100_total", precision = 15, scale = 6)
    private BigDecimal gwp100Total;
    
    @Column(name = "gwp_100_fossil", precision = 15, scale = 6)
    private BigDecimal gwp100Fossil;
    
    @Column(name = "gwp_100_biogenico", precision = 15, scale = 6)
    private BigDecimal gwp100Biogenico;
    
    @Column(name = "gwp_100_transformacao", precision = 15, scale = 6)
    private BigDecimal gwp100Transformacao;
    
    @Column(name = "dioxido_carbono_fossil", precision = 15, scale = 6)
    private BigDecimal dioxidoCarbonoFossil;
    
    @Column(name = "dioxido_carbono_metano_transformacao", precision = 15, scale = 6)
    private BigDecimal dioxidoCarbonoMetanoTransformacao;
    
    @Column(name = "metano_fossil", precision = 15, scale = 6)
    private BigDecimal metanoFossil;
    
    @Column(name = "metano_biogenico", precision = 15, scale = 6)
    private BigDecimal metanoBiogenico;
    
    @Column(name = "oxido_nitroso", precision = 15, scale = 6)
    private BigDecimal oxidoNitroso;
    
    @Column(name = "outras_substancias", precision = 15, scale = 6)
    private BigDecimal outrasSubstanciasEscopo3;
    
    @Column(name = "co2_ch4_transformacao", precision = 15, scale = 6)
    private BigDecimal co2Ch4Transformacao;
    
    // ESCOPO 3 - Observações
    @Column(columnDefinition = "TEXT")
    private String comentarios;
    
    // Campos de auditoria
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "ultima_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "versao", length = 10, nullable = false)
    private String versao = "v1";
    
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