package br.gov.serpro.calculadoraacv.enums;

public enum Bioma {
    AMAZONIA("amazonia", "Amazônia"),
    CERRADO("cerrado", "Cerrado"),
    CAATINGA("caatinga", "Caatinga"),
    MATA_ATLANTICA("mata-atlantica", "Mata Atlântica"),
    PAMPA("pampa", "Pampa"),
    PANTANAL("pantanal", "Pantanal");
    
    private final String codigo;
    private final String descricao;
    
    Bioma(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static Bioma fromCodigo(String codigo) {
        for (Bioma bioma : values()) {
            if (bioma.getCodigo().equals(codigo)) {
                return bioma;
            }
        }
        throw new IllegalArgumentException("Bioma inválido: " + codigo);
    }
    
    public static Bioma fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do bioma não pode ser nulo");
        }
        
        switch (valor.toUpperCase()) {
            case "AMAZONIA":
            case "AMAZÔNIA":
                return AMAZONIA;
            case "CERRADO":
                return CERRADO;
            case "CAATINGA":
                return CAATINGA;
            case "MATA_ATLANTICA":
            case "MATA ATLANTICA":
            case "MATA ATLÂNTICA":
                return MATA_ATLANTICA;
            case "PAMPA":
                return PAMPA;
            case "PANTANAL":
                return PANTANAL;
            default:
                return fromCodigo(valor.toLowerCase());
        }
    }
}