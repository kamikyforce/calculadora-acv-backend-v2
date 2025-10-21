package br.gov.serpro.calculadoraacv.enums;

public enum FazParteDieta {
    SIM("sim", "Sim"),
    NAO("nao", "Não");
    
    private final String codigo;
    private final String descricao;
    
    FazParteDieta(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static FazParteDieta fromCodigo(String codigo) {
        for (FazParteDieta opcao : values()) {
            if (opcao.getCodigo().equals(codigo)) {
                return opcao;
            }
        }
        throw new IllegalArgumentException("Opção de faz parte da dieta inválida: " + codigo);
    }
}