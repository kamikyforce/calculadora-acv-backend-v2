package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InsumoRebanhoRequest {
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotBlank(message = "Módulo é obrigatório")
    @Size(max = 100, message = "Módulo deve ter no máximo 100 caracteres")
    private String modulo;
    
    @NotNull(message = "Tipo é obrigatório")
    private TipoInsumo tipo;
    
    @NotBlank(message = "Escopo é obrigatório")
    @Size(max = 50, message = "Escopo deve ter no máximo 50 caracteres")
    private EscopoEnum escopo;
    
    @NotBlank(message = "Identificação do produto é obrigatória")
    @Size(max = 200, message = "Identificação do produto deve ter no máximo 200 caracteres")
    private String identificacaoProduto;
    
    @Size(max = 200, message = "Fonte do dataset deve ter no máximo 200 caracteres")
    private String fonteDataset;
    
    @Size(max = 255, message = "Dataset do produto deve ter no máximo 255 caracteres")
    private String datasetProduto;
    
    @NotNull(message = "Unidade do produto é obrigatória")
    private UnidadeProdutoReferencia unidadeProduto;
    
    @Size(max = 100, message = "Método de avaliação GWP deve ter no máximo 100 caracteres")
    private String metodoAvaliacaoGwp;
    
    private GrupoIngredienteAlimentar grupoIngrediente;
    
    @Size(max = 200, message = "Nome do produto deve ter no máximo 200 caracteres")
    private String nomeProduto;
    
    @Size(max = 100, message = "UUID deve ter no máximo 100 caracteres")
    private String uuid;
    
    @DecimalMin(value = "0.0", message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;

    @DecimalMin(value = "0.0", message = "Quantidade do produto de referência deve ser positiva")
    private BigDecimal quantidadeProdutoReferencia;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unidade;

    @DecimalMin(value = "0.0", message = "GWP 100 Fóssil deve ser positivo")
    private BigDecimal gwp100Fossil;
    
    @DecimalMin(value = "0.0", message = "GWP 100 Biogênico deve ser positivo")
    private BigDecimal gwp100Biogenico;
    
    @DecimalMin(value = "0.0", message = "GWP 100 Transformação deve ser positivo")
    private BigDecimal gwp100Transformacao;
    
    @DecimalMin(value = "0.0", message = "CO2 Fóssil deve ser positivo")
    private BigDecimal co2Fossil;
    
    @DecimalMin(value = "0.0", message = "CO2 e CH4 Transformação deve ser positivo")
    private BigDecimal co2Ch4Transformacao;
    
    @DecimalMin(value = "0.0", message = "CH4 Fóssil deve ser positivo")
    private BigDecimal ch4Fossil;
    
    @DecimalMin(value = "0.0", message = "CH4 Biogênico deve ser positivo")
    private BigDecimal ch4Biogenico;
    
    @DecimalMin(value = "0.0", message = "N2O deve ser positivo")
    private BigDecimal n2o;
    
    @DecimalMin(value = "0.0", message = "Outras substâncias deve ser positivo")
    private BigDecimal outrasSubstancias;
    
    private FazParteDieta fazParteDieta;
    
    @Size(max = 200, message = "Ingrediente deve ter no máximo 200 caracteres")
    private String ingrediente;
    
    @Size(max = 100, message = "NOT EU deve ter no máximo 100 caracteres")
    private String notEu;
    
    @DecimalMin(value = "0.0", message = "Energia bruta deve ser positiva")
    private BigDecimal energiaBruta;
    
    @DecimalMin(value = "0.0", message = "MS deve ser positivo")
    private BigDecimal ms;
    
    @DecimalMin(value = "0.0", message = "Proteína bruta deve ser positiva")
    private BigDecimal proteinaBruta;
    
    private FatoresEmissao fatoresEmissao;
    
    @Size(max = 1000, message = "Comentários deve ter no máximo 1000 caracteres")
    private String comentarios;
    
    @Size(max = 1000, message = "Comentários do Escopo 1 deve ter no máximo 1000 caracteres")
    private String comentariosEscopo1;
    
    @Size(max = 1000, message = "Comentários do Escopo 3 deve ter no máximo 1000 caracteres")
    private String comentariosEscopo3;
    
    @DecimalMin(value = "0.0", message = "GEE Total Escopo 1 deve ser positivo")
    private BigDecimal geeTotalEscopo1;
    
    @DecimalMin(value = "0.0", message = "CO2 Fóssil Escopo 1 deve ser positivo")
    private BigDecimal co2FossilEscopo1;
    
    @DecimalMin(value = "0.0", message = "Uso da Terra Escopo 1 deve ser positivo")
    private BigDecimal usoTerraEscopo1;
    
    @DecimalMin(value = "0.0", message = "CH4 Fóssil Escopo 1 deve ser positivo")
    private BigDecimal ch4FossilEscopo1;
    
    @DecimalMin(value = "0.0", message = "CH4 Biogênico Escopo 1 deve ser positivo")
    private BigDecimal ch4BiogenicoEscopo1;
    
    @DecimalMin(value = "0.0", message = "N2O Escopo 1 deve ser positivo")
    private BigDecimal n2oEscopo1;
    
    @DecimalMin(value = "0.0", message = "Outras Substâncias Escopo 1 deve ser positivo")
    private BigDecimal outrasSubstanciasEscopo1;

    @DecimalMin(value = "0.0", message = "GEE Total Escopo 3 deve ser positivo")
    private BigDecimal geeTotalEscopo3;
    
    @DecimalMin(value = "0.0", message = "GWP 100 Fóssil Escopo 3 deve ser positivo")
    private BigDecimal gwp100FossilEscopo3;
    
    @DecimalMin(value = "0.0", message = "GWP 100 Biogênico Escopo 3 deve ser positivo")
    private BigDecimal gwp100BiogenicoEscopo3;
    
    @DecimalMin(value = "0.0", message = "GWP 100 Transformação Escopo 3 deve ser positivo")
    private BigDecimal gwp100TransformacaoEscopo3;
    
    @DecimalMin(value = "0.0", message = "Dióxido de Carbono Fóssil Escopo 3 deve ser positivo")
    private BigDecimal dioxidoCarbonoFossilEscopo3;
    
    @DecimalMin(value = "0.0", message = "Dióxido de Carbono e Metano Transformação Escopo 3 deve ser positivo")
    private BigDecimal dioxidoCarbonoMetanoTransformacaoEscopo3;
    
    @DecimalMin(value = "0.0", message = "Metano Fóssil Escopo 3 deve ser positivo")
    private BigDecimal metanoFossilEscopo3;
    
    @DecimalMin(value = "0.0", message = "Metano Biogênico Escopo 3 deve ser positivo")
    private BigDecimal metanoBiogenicoEscopo3;
    
    @DecimalMin(value = "0.0", message = "Óxido Nitroso Escopo 3 deve ser positivo")
    private BigDecimal oxidoNitrosoEscopo3;
    
    @DecimalMin(value = "0.0", message = "Outras Substâncias Escopo 3 deve ser positivo")
    private BigDecimal outrasSubstanciasEscopo3;
    
    private Boolean ativo = true;
}