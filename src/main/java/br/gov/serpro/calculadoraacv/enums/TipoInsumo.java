package br.gov.serpro.calculadoraacv.enums;

public enum TipoInsumo {
    INGREDIENTES_ALIMENTARES("Ingredientes alimentares"),
    ANIMAIS_COMPRADOS("Animais comprados"),
    FERTILIZANTES("Fertilizantes");
    
    private final String descricao;
    
    TipoInsumo(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoInsumo fromDescricao(String descricao) {
        for (TipoInsumo tipo : values()) {
            if (tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de insumo inválido: " + descricao);
    }
}