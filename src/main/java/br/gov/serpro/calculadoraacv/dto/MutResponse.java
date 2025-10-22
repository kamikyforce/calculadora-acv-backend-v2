package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutResponse {
    
    private Long id;
    private TipoMudanca tipoMudanca;
    private EscopoEnum escopo;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String nomeUsuario;
    private List<DadosDesmatamentoResponse> dadosDesmatamento;
    private List<DadosVegetacaoResponse> dadosVegetacao;
    private List<DadosSoloResponse> dadosSolo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosDesmatamentoResponse {
        private Long id;
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
        private Boolean replicadoAutomatico;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosVegetacaoResponse {
        private Long id;
        private Bioma bioma;
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
        private Boolean replicadoAutomatico;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosSoloResponse {
        private Long id;
        private Bioma bioma;
        private TipoFatorSolo tipoFatorSolo;
        private BigDecimal valorFator;
        private BigDecimal fatorCO2;
        private BigDecimal fatorCH4;
        private BigDecimal fatorN2O;
        private String descricao;
        private String usoAnterior;
        private String usoAtual;
        private Boolean principal;
        private Boolean replicadoAutomatico;
    }
}