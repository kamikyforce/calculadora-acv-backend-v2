package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.*;
import br.gov.serpro.calculadoraacv.model.InsumoRebanho;
import lombok.Data;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
public class InsumoRebanhoResponse {
    
    private Long id;
    private String modulo;
    private TipoInsumo tipo;
    private EscopoEnum escopo;
    private String identificacaoProduto;
    private String fonteDataset;
    private String datasetProduto;
    private UnidadeProdutoReferencia unidadeProduto;
    private String metodoAvaliacaoGwp;
    
    private GrupoIngredienteAlimentar grupoIngrediente;
    private String nomeProduto;
    private String uuid;
    private BigDecimal quantidade;
    private BigDecimal quantidadeProdutoReferencia;
    private String unidade;

    private BigDecimal gwp100Fossil;
    private BigDecimal gwp100Biogenico;
    private BigDecimal gwp100Transformacao;
    private BigDecimal co2Fossil;
    private BigDecimal co2Ch4Transformacao;
    private BigDecimal ch4Fossil;
    private BigDecimal ch4Biogenico;
    private BigDecimal n2o;
    private BigDecimal outrasSubstancias;
    
    private FazParteDieta fazParteDieta;
    private String ingrediente;
    private String notEu;
    private BigDecimal energiaBruta;
    private BigDecimal ms;
    private BigDecimal proteinaBruta;
    private FatoresEmissao fatoresEmissao;
    
    private String comentarios;
    private String comentariosEscopo1;
    private String comentariosEscopo3;
    
    private BigDecimal geeTotalEscopo1;
    private BigDecimal co2FossilEscopo1;
    private BigDecimal usoTerraEscopo1;
    private BigDecimal ch4FossilEscopo1;
    private BigDecimal ch4BiogenicoEscopo1;
    private BigDecimal n2oEscopo1;
    private BigDecimal outrasSubstanciasEscopo1;

    private BigDecimal geeTotalEscopo3;
    private BigDecimal gwp100FossilEscopo3;
    private BigDecimal gwp100BiogenicoEscopo3;
    private BigDecimal gwp100TransformacaoEscopo3;
    private BigDecimal dioxidoCarbonoFossilEscopo3;
    private BigDecimal dioxidoCarbonoMetanoTransformacaoEscopo3;
    private BigDecimal metanoFossilEscopo3;
    private BigDecimal metanoBiogenicoEscopo3;
    private BigDecimal oxidoNitrosoEscopo3;
    private BigDecimal outrasSubstanciasEscopo3;
    
    private Boolean ativo;
    private String dataCriacao;
    private String dataAtualizacao;
    
    public InsumoRebanhoResponse(InsumoRebanho entidade) {
        this.id = entidade.getId();
        this.modulo = entidade.getModulo();
        this.tipo = entidade.getTipo();
        this.escopo = entidade.getEscopo();
        this.identificacaoProduto = entidade.getIdentificacaoProduto();
        this.fonteDataset = entidade.getFonteDataset();
        this.datasetProduto = entidade.getDatasetProduto();
        this.unidadeProduto = entidade.getUnidadeProduto();
        this.metodoAvaliacaoGwp = entidade.getMetodoAvaliacaoGwp();
        
        this.grupoIngrediente = entidade.getGrupoIngrediente();
        this.nomeProduto = entidade.getNomeProduto();
        this.uuid = entidade.getUuid();
        this.quantidade = entidade.getQuantidade();
        this.quantidadeProdutoReferencia = entidade.getQuantidadeProdutoReferencia();
        this.unidade = entidade.getUnidade();

        this.gwp100Fossil = entidade.getGwp100Fossil();
        this.gwp100Biogenico = entidade.getGwp100Biogenico();
        this.gwp100Transformacao = entidade.getGwp100Transformacao();
        this.co2Fossil = entidade.getCo2Fossil();
        this.co2Ch4Transformacao = entidade.getCo2Ch4Transformacao();
        this.ch4Fossil = entidade.getCh4Fossil();
        this.ch4Biogenico = entidade.getCh4Biogenico();
        this.n2o = entidade.getN2o();
        this.outrasSubstancias = entidade.getOutrasSubstancias();
        
        this.fazParteDieta = entidade.getFazParteDieta();
        this.ingrediente = entidade.getIngrediente();
        this.notEu = entidade.getNotEu();
        this.energiaBruta = entidade.getEnergiaBruta();
        this.ms = entidade.getMs();
        this.proteinaBruta = entidade.getProteinaBruta();
        this.fatoresEmissao = entidade.getFatoresEmissao();
        
        this.comentarios = entidade.getComentarios();
        this.comentariosEscopo1 = entidade.getComentariosEscopo1();
        this.comentariosEscopo3 = entidade.getComentariosEscopo3();
        
        this.geeTotalEscopo1 = entidade.getGeeTotalEscopo1();
        this.co2FossilEscopo1 = entidade.getCo2FossilEscopo1();
        this.usoTerraEscopo1 = entidade.getUsoTerraEscopo1();
        this.ch4FossilEscopo1 = entidade.getCh4FossilEscopo1();
        this.ch4BiogenicoEscopo1 = entidade.getCh4BiogenicoEscopo1();
        this.n2oEscopo1 = entidade.getN2oEscopo1();
        this.outrasSubstanciasEscopo1 = entidade.getOutrasSubstanciasEscopo1();
        
        this.geeTotalEscopo3 = entidade.getGeeTotalEscopo3();
        this.gwp100FossilEscopo3 = entidade.getGwp100FossilEscopo3();
        this.gwp100BiogenicoEscopo3 = entidade.getGwp100BiogenicoEscopo3();
        this.gwp100TransformacaoEscopo3 = entidade.getGwp100TransformacaoEscopo3();
        this.dioxidoCarbonoFossilEscopo3 = entidade.getDioxidoCarbonoFossilEscopo3();
        this.dioxidoCarbonoMetanoTransformacaoEscopo3 = entidade.getDioxidoCarbonoMetanoTransformacaoEscopo3();
        this.metanoFossilEscopo3 = entidade.getMetanoFossilEscopo3();
        this.metanoBiogenicoEscopo3 = entidade.getMetanoBiogenicoEscopo3();
        this.oxidoNitrosoEscopo3 = entidade.getOxidoNitrosoEscopo3();
        this.outrasSubstanciasEscopo3 = entidade.getOutrasSubstanciasEscopo3();
        
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