package br.gov.serpro.calculadoraacv.enums;

public enum CategoriaDesmatamento {
    O("o", "O"),
    F("f", "F"),
    OFL("ofl", "OFL"),
    G("g", "G");
    
    private final String codigo;
    private final String descricao;
    
    CategoriaDesmatamento(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static CategoriaDesmatamento fromCodigo(String codigo) {
        for (CategoriaDesmatamento categoria : values()) {
            if (categoria.getCodigo().equals(codigo)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria de desmatamento inválida: " + codigo);
    }
    
    public static CategoriaDesmatamento fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da categoria de desmatamento não pode ser nulo");
        }
        
        switch (valor.toUpperCase()) {
            case "O":
                return O;
            case "F":
                return F;
            case "OFL":
                return OFL;
            case "G":
                return G;
            default:
                return fromCodigo(valor.toLowerCase());
        }
    }
}