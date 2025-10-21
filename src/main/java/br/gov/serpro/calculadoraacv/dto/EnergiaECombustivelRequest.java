package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import br.gov.serpro.calculadoraacv.enums.TipoDado;

public class EnergiaECombustivelRequest {
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotBlank(message = "Tipo de energia é obrigatório")
    @Size(max = 50, message = "Tipo de energia deve ter no máximo 50 caracteres")
    private String tipoEnergia;
    
    @Size(max = 100, message = "Fonte de energia deve ter no máximo 100 caracteres")
    private String fonteEnergia;
    
    // CAMPO CONDICIONAL: obrigatório apenas para CONSOLIDADO_ANUAL
    @DecimalMin(value = "0.0", inclusive = false, message = "Consumo anual deve ser positivo")
    private BigDecimal consumoAnual;
    
    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unidade;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Fator de emissão deve ser positivo")
    private BigDecimal fatorEmissao;
    
    @NotBlank(message = "Escopo é obrigatório")
    @Pattern(regexp = "^(escopo1|escopo2|escopo3-producao|escopo3-transporte|ESCOPO1|ESCOPO2|ESCOPO3)$", 
             message = "Escopo deve ser: escopo1, escopo2, escopo3-producao, escopo3-transporte, ESCOPO1, ESCOPO2 ou ESCOPO3")
    private String escopo;
    
    @NotNull(message = "Ano de referência é obrigatório")
    @Min(value = 1900, message = "Ano de referência deve ser maior ou igual a 1900")
    @Max(value = 2100, message = "Ano de referência deve ser menor ou igual a 2100")
    private Integer anoReferencia;
    
    // NOVOS CAMPOS PARA IMPLEMENTAÇÃO
    @NotNull(message = "Tipo de dado é obrigatório")
    private TipoDado tipoDado;
    
    // DADOS MENSAIS - Lista de 12 valores (Janeiro a Dezembro)
    @Size(max = 12, message = "Máximo 12 meses")
    private List<DadoMensal> dadosMensais;
    
    // CAMPOS DE VERSIONAMENTO E AUDITORIA
    private Integer versao = 1;
    private String observacoesAuditoria;
    
    // Getters e Setters
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
    
    public BigDecimal getFatorEmissao() { return fatorEmissao; }
    public void setFatorEmissao(BigDecimal fatorEmissao) { this.fatorEmissao = fatorEmissao; }
    
    // Adicionar após linha 26 (após fatorEmissao)
    @DecimalMin(value = "0.0", inclusive = false, message = "Fator médio anual deve ser positivo")
    private BigDecimal fatorMedioAnual;
    
    // Adicionar getter e setter após linha 65
    public BigDecimal getFatorMedioAnual() { return fatorMedioAnual; }
    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) { this.fatorMedioAnual = fatorMedioAnual; }
    public String getEscopo() { return escopo; }
    public void setEscopo(String escopo) { this.escopo = escopo; }
    
    public Integer getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(Integer anoReferencia) { this.anoReferencia = anoReferencia; }
    
    // NOVOS GETTERS E SETTERS
    public TipoDado getTipoDado() { return tipoDado; }
    public void setTipoDado(TipoDado tipoDado) { this.tipoDado = tipoDado; }
    
    public List<DadoMensal> getDadosMensais() { return dadosMensais; }
    public void setDadosMensais(List<DadoMensal> dadosMensais) { this.dadosMensais = dadosMensais; }
    
    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
    
    public String getObservacoesAuditoria() { return observacoesAuditoria; }
    public void setObservacoesAuditoria(String observacoesAuditoria) { this.observacoesAuditoria = observacoesAuditoria; }
    
    // CLASSE INTERNA PARA DADOS MENSAIS
    public static class DadoMensal {
        @Min(value = 1, message = "Mês deve ser entre 1 e 12")
        @Max(value = 12, message = "Mês deve ser entre 1 e 12")
        private Integer mes;
        
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser positivo")
        private BigDecimal valor;
        
        // Getters e Setters
        public Integer getMes() { return mes; }
        public void setMes(Integer mes) { this.mes = mes; }
        
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
    }
}
