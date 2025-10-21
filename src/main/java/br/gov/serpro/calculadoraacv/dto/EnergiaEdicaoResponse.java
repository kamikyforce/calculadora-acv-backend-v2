package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.StatusCalculo;
import br.gov.serpro.calculadoraacv.enums.TipoDado;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO específico para edição de dados de energia/combustível
 * Direciona o usuário baseado no tipo de dado (CONSOLIDADO_ANUAL vs MENSAL)
 */
public class EnergiaEdicaoResponse {
    
    // Dados básicos
    private Long id;
    private Long usuarioId;
    private String tipoEnergia;
    private String fonteEnergia;
    private String unidade;
    private BigDecimal fatorEmissao;
    private String escopo;
    private Integer anoReferencia;
    private TipoDado tipoDado;
    private Integer versao;
    private String observacoesAuditoria;
    
    // Direcionamento de edição
    private String modoEdicao; // "CONSOLIDADO" ou "MENSAL"
    private String instrucoes;
    
    // Para CONSOLIDADO_ANUAL
    private BigDecimal consumoAnual;
    private BigDecimal fatorMedioAnual;
    
    // Para MENSAL
    private List<DadoMensalEdicao> dadosMensais;
    private StatusCalculo statusCalculo;
    private Integer mesesPreenchidos;
    private BigDecimal mediaAnualCalculada;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getTipoEnergia() { return tipoEnergia; }
    public void setTipoEnergia(String tipoEnergia) { this.tipoEnergia = tipoEnergia; }
    
    public String getFonteEnergia() { return fonteEnergia; }
    public void setFonteEnergia(String fonteEnergia) { this.fonteEnergia = fonteEnergia; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    public BigDecimal getFatorEmissao() { return fatorEmissao; }
    public void setFatorEmissao(BigDecimal fatorEmissao) { this.fatorEmissao = fatorEmissao; }
    
    public String getEscopo() { return escopo; }
    public void setEscopo(String escopo) { this.escopo = escopo; }
    
    public Integer getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(Integer anoReferencia) { this.anoReferencia = anoReferencia; }
    
    public TipoDado getTipoDado() { return tipoDado; }
    public void setTipoDado(TipoDado tipoDado) { this.tipoDado = tipoDado; }
    
    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
    
    public String getObservacoesAuditoria() { return observacoesAuditoria; }
    public void setObservacoesAuditoria(String observacoesAuditoria) { this.observacoesAuditoria = observacoesAuditoria; }
    
    public String getModoEdicao() { return modoEdicao; }
    public void setModoEdicao(String modoEdicao) { this.modoEdicao = modoEdicao; }
    
    public String getInstrucoes() { return instrucoes; }
    public void setInstrucoes(String instrucoes) { this.instrucoes = instrucoes; }
    
    public BigDecimal getConsumoAnual() { return consumoAnual; }
    public void setConsumoAnual(BigDecimal consumoAnual) { this.consumoAnual = consumoAnual; }
    
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    
    public List<DadoMensalEdicao> getDadosMensais() { return dadosMensais; }
    public void setDadosMensais(List<DadoMensalEdicao> dadosMensais) { this.dadosMensais = dadosMensais; }
    
    public StatusCalculo getStatusCalculo() { return statusCalculo; }
    public void setStatusCalculo(StatusCalculo statusCalculo) { this.statusCalculo = statusCalculo; }
    
    public Integer getMesesPreenchidos() { return mesesPreenchidos; }
    public void setMesesPreenchidos(Integer mesesPreenchidos) { this.mesesPreenchidos = mesesPreenchidos; }
    
    public BigDecimal getMediaAnualCalculada() { return mediaAnualCalculada; }
    public void setMediaAnualCalculada(BigDecimal mediaAnualCalculada) { this.mediaAnualCalculada = mediaAnualCalculada; }
}