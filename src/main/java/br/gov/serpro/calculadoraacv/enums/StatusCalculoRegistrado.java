package br.gov.serpro.calculadoraacv.enums;

public enum StatusCalculoRegistrado {
    CONCLUIDO("CONCLUIDO", "Concluído"),
    RASCUNHO("RASCUNHO", "Rascunho");
    
    private final String codigo;
    private final String descricao;
    
    StatusCalculoRegistrado(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() { 
        return codigo; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public static StatusCalculoRegistrado fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do status não pode ser nulo");
        }
        
        switch (valor.toUpperCase()) {
            case "CONCLUÍDO":
            case "CONCLUIDO":
                return CONCLUIDO;
            case "RASCUNHO":
                return RASCUNHO;
            default:
                throw new IllegalArgumentException("Status inválido: " + valor);
        }
    }
}