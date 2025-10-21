package br.gov.serpro.calculadoraacv.enums;

public enum UnidadeProducaoAgricola {
    KG("kg"),
    T("t"),
    G("g"),
    L("L"),
    ML("mL"),
    KWH("kWh"),
    MJ("MJ"),
    HA("ha"),
    M2("m²"),
    UNIDADE("unidade");
    
    private final String descricao;
    
    UnidadeProducaoAgricola(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static UnidadeProducaoAgricola fromDescricao(String descricao) {
        for (UnidadeProducaoAgricola unidade : values()) {
            if (unidade.getDescricao().equals(descricao)) {
                return unidade;
            }
        }
        throw new IllegalArgumentException("Unidade de produção agrícola inválida: " + descricao);
    }
}