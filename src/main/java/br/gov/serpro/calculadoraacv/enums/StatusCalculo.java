package br.gov.serpro.calculadoraacv.enums;

public enum StatusCalculo {
    PENDENTE("PENDENTE", "Cálculo pendente"),
    PARCIAL("PARCIAL", "Cálculo parcial - menos de 12 meses"),
    COMPLETO("COMPLETO", "Cálculo completo - 12 meses preenchidos");
    
    private final String codigo;
    private final String descricao;
    
    StatusCalculo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
}