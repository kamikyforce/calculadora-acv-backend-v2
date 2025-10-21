package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
public class InsumoProducaoAgricolaRequest {
    
    // Campos de controle
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    private String versao;
    
    // ESCOPO 1 - Classificação
    private String classe;
    private String especificacao;
    
    // ESCOPO 1 - Teor de macronutrientes
    private BigDecimal teorNitrogenio;
    private BigDecimal teorFosforo;
    private BigDecimal teorPotassio;
    
    // ESCOPO 1 - Fator de conversão
    private BigDecimal fatorConversao;
    @JsonProperty("unidadeFatorConversao")
    private String fatorConversaoUnidade;
    
    // ESCOPO 1 - Quantidade e unidade de referência
    private BigDecimal quantidade;
    private String unidadeReferencia;
    
    // ESCOPO 1 - Fatores de emissão
    private BigDecimal feCo2Biogenico;
    private String refFeCo2Biogenico;
    private BigDecimal feCo2;
    private String refFeCo2;
    private BigDecimal feCh4;
    private String refFeCh4;
    private BigDecimal feN2oDireto;
    private String refFeN2oDireto;
    private BigDecimal fracN2oVolatilizacao;
    private String refFracN2oVolatilizacao;
    private BigDecimal fracN2oLixiviacao;
    private String refFracN2oLixiviacao;
    private BigDecimal feN2oComposto;
    private String refFeN2oComposto;
    private BigDecimal feCo;
    private String refFeCo;
    private BigDecimal feNox;
    private String refFeNox;
    
    // ESCOPO 3 - Identificação e classificação
    private String grupoIngrediente;
    private String nomeProduto;
    private String tipoProduto;
    
    // ESCOPO 3 - Quantidade e unidade de referência
    @JsonProperty("qtdProdutoReferencia")
    private BigDecimal quantidadeProdutoReferencia;
    private String unidadeProdutoReferencia;
    private String unidadeProduto;
    
    // ESCOPO 3 - Quantidade e unidade
    private BigDecimal quantidadeProduto;
    
    // ESCOPO 3 - Valores de emissões (GEE)
    private BigDecimal geeTotal;
    private BigDecimal gwp100Total;
    private BigDecimal gwp100Fossil;
    private BigDecimal gwp100Biogenico;
    private BigDecimal gwp100Transformacao;
    private BigDecimal dioxidoCarbonoFossil;
    private BigDecimal dioxidoCarbonoMetanoTransformacao;
    private BigDecimal co2Ch4Transformacao;
    private BigDecimal metanoFossil;
    private BigDecimal metanoBiogenico;
    private BigDecimal oxidoNitroso;
    @JsonProperty("outrasSubstancias")
    private BigDecimal outrasSubstanciasEscopo3;
    
    // ESCOPO 3 - Observações
    @Size(max = 1000, message = "Comentários deve ter no máximo 1000 caracteres")
    private String comentarios;
    
    // Campos de auditoria
    private Boolean ativo = true;
}