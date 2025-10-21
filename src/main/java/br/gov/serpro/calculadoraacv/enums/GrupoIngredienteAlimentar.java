package br.gov.serpro.calculadoraacv.enums;

public enum GrupoIngredienteAlimentar {
    CEREAIS_E_GRAOS("Cereais e grãos"),
    LEGUMINOSAS("Leguminosas"),
    OLEAGINOSAS("Oleaginosas");
    
    private final String descricao;
    
    GrupoIngredienteAlimentar(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static GrupoIngredienteAlimentar fromDescricao(String descricao) {
        for (GrupoIngredienteAlimentar grupo : values()) {
            if (grupo.getDescricao().equals(descricao)) {
                return grupo;
            }
        }
        throw new IllegalArgumentException("Grupo de ingrediente alimentar inválido: " + descricao);
    }
}