package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.InsumoProducaoAgricola;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
public class InsumoProducaoAgricolaResponse {
    
    // Campos de controle
    private Long id;
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
    private BigDecimal metanoFossil;
    private BigDecimal metanoBiogenico;
    private BigDecimal oxidoNitroso;
    private BigDecimal outrasSubstanciasEscopo3;
    private BigDecimal co2Ch4Transformacao;
    
    // ESCOPO 3 - Observações
    private String comentarios;
    
    // Campos de auditoria
    private Boolean ativo;
    private String dataCriacao;
    
    @JsonProperty("ultimaAtualizacao")
    private String dataAtualizacao;
    
    public InsumoProducaoAgricolaResponse(InsumoProducaoAgricola entidade) {
        // Campos de controle
        this.id = entidade.getId();
        this.usuarioId = entidade.getUsuarioId();
        
        this.versao = entidade.getVersao();
        
        // ESCOPO 1 - Classificação
        this.classe = entidade.getClasse();
        this.especificacao = entidade.getEspecificacao();
        
        // ESCOPO 1 - Teor de macronutrientes
        this.teorNitrogenio = entidade.getTeorNitrogenio();
        this.teorFosforo = entidade.getTeorFosforo();
        this.teorPotassio = entidade.getTeorPotassio();
        
        // ESCOPO 1 - Fator de conversão
        this.fatorConversao = entidade.getFatorConversao();
        this.fatorConversaoUnidade = entidade.getFatorConversaoUnidade();
        
        // ESCOPO 1 - Quantidade e unidade de referência
        this.quantidade = entidade.getQuantidade();
        this.unidadeReferencia = entidade.getUnidadeReferencia();
        
        // ESCOPO 1 - Fatores de emissão
        this.feCo2Biogenico = entidade.getFeCo2Biogenico();
        this.refFeCo2Biogenico = entidade.getRefFeCo2Biogenico();
        this.feCo2 = entidade.getFeCo2();
        this.refFeCo2 = entidade.getRefFeCo2();
        this.feCh4 = entidade.getFeCh4();
        this.refFeCh4 = entidade.getRefFeCh4();
        this.feN2oDireto = entidade.getFeN2oDireto();
        this.refFeN2oDireto = entidade.getRefFeN2oDireto();
        this.fracN2oVolatilizacao = entidade.getFracN2oVolatilizacao();
        this.refFracN2oVolatilizacao = entidade.getRefFracN2oVolatilizacao();
        this.fracN2oLixiviacao = entidade.getFracN2oLixiviacao();
        this.refFracN2oLixiviacao = entidade.getRefFracN2oLixiviacao();
        this.feN2oComposto = entidade.getFeN2oComposto();
        this.refFeN2oComposto = entidade.getRefFeN2oComposto();
        this.feCo = entidade.getFeCo();
        this.refFeCo = entidade.getRefFeCo();
        this.feNox = entidade.getFeNox();
        this.refFeNox = entidade.getRefFeNox();
        
        // ESCOPO 3 - Identificação e classificação
        this.grupoIngrediente = entidade.getGrupoIngrediente();
        this.nomeProduto = entidade.getNomeProduto();
        this.tipoProduto = entidade.getTipoProduto();
        
        // ESCOPO 3 - Quantidade e unidade de referência
        this.quantidadeProdutoReferencia = entidade.getQuantidadeProdutoReferencia();
        this.unidadeProdutoReferencia = entidade.getUnidadeProdutoReferencia();
        this.unidadeProduto = entidade.getUnidadeProduto();
        
        // ESCOPO 3 - Quantidade e unidade
        this.quantidadeProduto = entidade.getQuantidadeProduto();
        
        // ESCOPO 3 - Valores de emissões (GEE)
        this.geeTotal = entidade.getGeeTotal();
        this.gwp100Total = entidade.getGwp100Total();
        this.gwp100Fossil = entidade.getGwp100Fossil();
        this.gwp100Biogenico = entidade.getGwp100Biogenico();
        this.gwp100Transformacao = entidade.getGwp100Transformacao();
        this.dioxidoCarbonoFossil = entidade.getDioxidoCarbonoFossil();
        this.dioxidoCarbonoMetanoTransformacao = entidade.getDioxidoCarbonoMetanoTransformacao();
        this.metanoFossil = entidade.getMetanoFossil();
        this.metanoBiogenico = entidade.getMetanoBiogenico();
        this.oxidoNitroso = entidade.getOxidoNitroso();
        this.outrasSubstanciasEscopo3 = entidade.getOutrasSubstanciasEscopo3();
        this.co2Ch4Transformacao = entidade.getCo2Ch4Transformacao();
        
        // ESCOPO 3 - Observações
        this.comentarios = entidade.getComentarios();
        
        // Campos de auditoria
        this.ativo = entidade.getAtivo();
        
        if (entidade.getDataCriacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            this.dataCriacao = entidade.getDataCriacao().format(formatter);
        }
        
        if (entidade.getDataAtualizacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            this.dataAtualizacao = entidade.getDataAtualizacao().format(formatter);
        }
    }
}