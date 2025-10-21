package br.gov.serpro.calculadoraacv.enums;

public enum TipoProducaoAgricola {
    FERTILIZANTES("Fertilizantes"),
    FERTILIZANTE("Fertilizante"),
    DEFENSIVOS("Defensivos"),
    DEFENSIVO("Defensivo"),
    PESTICIDAS("Pesticidas"),
    SEMENTES("Sementes"),
    SEMENTE("Semente"),
    COMBUSTIVEIS("Combustíveis"),
    COMBUSTIVEL("Combustível"),
    ENERGIA("Energia"),
    MAQUINARIO("Maquinário"),
    OUTRO("Outro");
    
    private final String descricao;
    
    TipoProducaoAgricola(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoProducaoAgricola fromDescricao(String descricao) {
        for (TipoProducaoAgricola tipo : values()) {
            if (tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de produção agrícola inválido: " + descricao);
    }
}