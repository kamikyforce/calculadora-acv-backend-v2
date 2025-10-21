package br.gov.serpro.calculadoraacv.enums;

public enum UnidadeProdutoReferencia {
    KG("kg"),
    T("t"),
    G("g");
    
    private final String descricao;
    
    UnidadeProdutoReferencia(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static UnidadeProdutoReferencia fromDescricao(String descricao) {
        for (UnidadeProdutoReferencia unidade : values()) {
            if (unidade.getDescricao().equals(descricao)) {
                return unidade;
            }
        }
        throw new IllegalArgumentException("Unidade de produto de referência inválida: " + descricao);
    }
}