package br.gov.serpro.calculadoraacv.dto;

import java.math.BigDecimal;

/**
 * DTO para representar um dado mensal durante a edição
 */
public class DadoMensalEdicao {
    private Integer mes;
    private BigDecimal valor;
    private String nomesMes; // "Janeiro", "Fevereiro", etc. (opcional)
    
    public DadoMensalEdicao() {}
    
    public DadoMensalEdicao(Integer mes, BigDecimal valor) {
        this.mes = mes;
        this.valor = valor;
        this.nomesMes = getNomeMes(mes);
    }
    
    private String getNomeMes(Integer mes) {
        if (mes == null) return null;
        String[] nomes = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                         "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        return (mes >= 1 && mes <= 12) ? nomes[mes - 1] : "Mês " + mes;
    }
    
    // Getters e Setters
    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { 
        this.mes = mes; 
        this.nomesMes = getNomeMes(mes);
    }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getNomesMes() { return nomesMes; }
    public void setNomesMes(String nomesMes) { this.nomesMes = nomesMes; }
}