package br.gov.serpro.calculadoraacv.enums;

public enum UF {
    AC("AC", "Acre"),
    AL("AL", "Alagoas"),
    AP("AP", "Amapá"),
    AM("AM", "Amazonas"),
    BA("BA", "Bahia"),
    CE("CE", "Ceará"),
    DF("DF", "Distrito Federal"),
    ES("ES", "Espírito Santo"),
    GO("GO", "Goiás"),
    MA("MA", "Maranhão"),
    MT("MT", "Mato Grosso"),
    MS("MS", "Mato Grosso do Sul"),
    MG("MG", "Minas Gerais"),
    PA("PA", "Pará"),
    PB("PB", "Paraíba"),
    PR("PR", "Paraná"),
    PE("PE", "Pernambuco"),
    PI("PI", "Piauí"),
    RJ("RJ", "Rio de Janeiro"),
    RN("RN", "Rio Grande do Norte"),
    RS("RS", "Rio Grande do Sul"),
    RO("RO", "Rondônia"),
    RR("RR", "Roraima"),
    SC("SC", "Santa Catarina"),
    SP("SP", "São Paulo"),
    SE("SE", "Sergipe"),
    TO("TO", "Tocantins");
    
    private final String codigo;
    private final String descricao;
    
    UF(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static UF fromCodigo(String codigo) {
        for (UF uf : values()) {
            if (uf.getCodigo().equalsIgnoreCase(codigo)) {
                return uf;
            }
        }
        throw new IllegalArgumentException("UF inválida: " + codigo);
    }
    
    public static UF fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da UF não pode ser nulo");
        }
        return fromCodigo(valor.toUpperCase());
    }
}