package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutRequest {
    
    private TipoMudanca tipoMudanca;
    private EscopoEnum escopo;
    private List<DadosDesmatamentoRequest> dadosDesmatamento;
    private List<DadosVegetacaoRequest> dadosVegetacao;
    private List<DadosSoloRequest> dadosSolo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosDesmatamentoRequest {
        private Bioma bioma;
        private Boolean valorUnico;
        private Set<UF> ufs;
        private String nomeFitofisionomia;
        private SiglaFitofisionomia siglaFitofisionomia;
        private CategoriaDesmatamento categoriaDesmatamento;
        private BigDecimal estoqueCarbono;
        private BigDecimal fatorCO2;
        private BigDecimal fatorCH4;
        private BigDecimal fatorN2O;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosVegetacaoRequest {
        private Bioma bioma; // Campo opcional para vegetação
        private Set<CategoriaDesmatamento> categoriasFitofisionomia;
        private String parametro;
        private BigDecimal valorAmazonia;
        private BigDecimal valorCaatinga;
        private BigDecimal valorCerrado;
        private BigDecimal valorMataAtlantica;
        private BigDecimal valorPampa;
        private BigDecimal valorPantanal;
        private BigDecimal fatorCO2;
        private BigDecimal fatorCH4;
        private BigDecimal fatorN2O;
        // ✅ NOVOS CAMPOS
        private String especieVegetacao;
        private BigDecimal alturaMedia;
        private BigDecimal biomassaAerea;
        private BigDecimal biomassaSubterranea;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosSoloRequest {
        private TipoFatorSolo tipoFatorSolo;
        private BigDecimal valorFator;
        private String descricao;
        private Bioma bioma;
        private BigDecimal fatorCO2;
        private BigDecimal fatorCH4;
        private BigDecimal fatorN2O;
        private String usoAnterior;
        private String usoAtual;
    }
}