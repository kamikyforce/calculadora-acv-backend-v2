package br.gov.serpro.calculadoraacv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnergiaComFatorResponse {
    private Long id;
    private Long usuarioId;
    private String tipoEnergia;
    private String fonteEnergia;
    private BigDecimal consumoAnual;
    private String unidade;
    private String escopo;
    private Integer ano;
    private BigDecimal fatorMedioAnual;
    private String tipoEmissao;
    private String fonteEmissao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public EnergiaComFatorResponse() {}
    
    // NOVO CAMPO PARA ANO DE REFERÊNCIA EFETIVO
    private Integer anoReferenciaEfetivo;
    private Integer anoReferencia; // Ano definido pelo usuário (pode ser null)
    
    // 👇 NOVO CAMPO PARA DADOS MENSAIS JSON
    private String dadosMensaisJson;
    private BigDecimal mediaAnualCalculada;
    private Integer mesesPreenchidos;
    private String statusCalculo;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getTipoEnergia() { return tipoEnergia; }
    public void setTipoEnergia(String tipoEnergia) { this.tipoEnergia = tipoEnergia; }
    
    public String getFonteEnergia() { return fonteEnergia; }
    public void setFonteEnergia(String fonteEnergia) { this.fonteEnergia = fonteEnergia; }
    
    public BigDecimal getConsumoAnual() { return consumoAnual; }
    public void setConsumoAnual(BigDecimal consumoAnual) { this.consumoAnual = consumoAnual; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    public String getEscopo() { return escopo; }
    public void setEscopo(String escopo) { this.escopo = escopo; }
    
    // GETTER DEFENSIVO para campo 'ano'
    // NOVO CAMPO para transparência na UI
    private Boolean anoAssumido = false;
    
    // GETTER DEFENSIVO para campo 'ano' (mantém compatibilidade)
    public Integer getAno() { 
        // Prioridade: ano -> anoReferenciaEfetivo -> anoReferencia
        if (ano != null) return ano;
        if (anoReferenciaEfetivo != null) return anoReferenciaEfetivo;
        return anoReferencia;
    }
    
    public void setAno(Integer ano) { 
        this.ano = ano; 
    }
    
    // Getters e Setters para transparência
    public Boolean getAnoAssumido() { return anoAssumido; }
    public void setAnoAssumido(Boolean anoAssumido) { this.anoAssumido = anoAssumido; }
    
    // Método utilitário para UI
    public String getAnoDisplay() {
        Integer anoAtual = getAno();
        if (anoAtual == null) return "Sem ano definido";
        return anoAssumido ? anoAtual + " (assumido)" : anoAtual.toString();
    }
    
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    
    public String getTipoEmissao() { return tipoEmissao; }
    public void setTipoEmissao(String tipoEmissao) { this.tipoEmissao = tipoEmissao; }
    
    public String getFonteEmissao() { return fonteEmissao; }
    public void setFonteEmissao(String fonteEmissao) { this.fonteEmissao = fonteEmissao; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    // Getters e Setters
    public Integer getAnoReferenciaEfetivo() { return anoReferenciaEfetivo; }
    public void setAnoReferenciaEfetivo(Integer anoReferenciaEfetivo) { this.anoReferenciaEfetivo = anoReferenciaEfetivo; }
    
    public Integer getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(Integer anoReferencia) { this.anoReferencia = anoReferencia; }
    
    // 👇 GETTER E SETTER PARA DADOS MENSAIS JSON
    public String getDadosMensaisJson() { return dadosMensaisJson; }
    public void setDadosMensaisJson(String dadosMensaisJson) { this.dadosMensaisJson = dadosMensaisJson; }
    
    // Getters e Setters para os novos campos
    public BigDecimal getMediaAnualCalculada() { return mediaAnualCalculada; }
    public void setMediaAnualCalculada(BigDecimal mediaAnualCalculada) { this.mediaAnualCalculada = mediaAnualCalculada; }
    
    public Integer getMesesPreenchidos() { return mesesPreenchidos; }
    public void setMesesPreenchidos(Integer mesesPreenchidos) { this.mesesPreenchidos = mesesPreenchidos; }
    
    public String getStatusCalculo() { return statusCalculo; }
    public void setStatusCalculo(String statusCalculo) { this.statusCalculo = statusCalculo; }
}