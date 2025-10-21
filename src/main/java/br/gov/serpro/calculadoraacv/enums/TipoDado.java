package br.gov.serpro.calculadoraacv.enums;

public enum TipoDado {
    CONSOLIDADO_ANUAL("CONSOLIDADO_ANUAL", "Dado Consolidado do Ano - Valor já calculado pelo governo"),
    MENSAL("MENSAL", "Dado Mensal - Dados mensais para cálculo de média anual");
    
    private final String codigo;
    private final String descricao;
    
    TipoDado(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
}