package br.gov.serpro.calculadoraacv.enums;

public enum FatoresEmissao {
    CALCULADO("Calculado"),
    ESTIMADO("Estimado");
    
    private final String descricao;
    
    FatoresEmissao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static FatoresEmissao fromDescricao(String descricao) {
        for (FatoresEmissao fator : values()) {
            if (fator.getDescricao().equals(descricao)) {
                return fator;
            }
        }
        throw new IllegalArgumentException("Fator de emissão inválido: " + descricao);
    }
}