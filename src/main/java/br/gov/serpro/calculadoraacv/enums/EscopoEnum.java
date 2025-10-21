package br.gov.serpro.calculadoraacv.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum EscopoEnum {
    ESCOPO1("escopo1", "Escopo 1 - Emissões Diretas"),
    ESCOPO2("escopo2", "Escopo 2 - Emissões Indiretas de Energia"),
    ESCOPO3("escopo3", "Escopo 3 - Emissões Indiretas"),
    ESCOPO3_PRODUCAO("escopo3-producao", "Escopo 3 - Produção"),
    ESCOPO3_TRANSPORTE("escopo3-transporte", "Escopo 3 - Transporte");
    
    private final String codigo;
    private final String descricao;
    
    EscopoEnum(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static EscopoEnum fromCodigo(String codigo) {
        for (EscopoEnum escopo : values()) {
            if (escopo.getCodigo().equals(codigo)) {
                return escopo;
            }
        }
        throw new IllegalArgumentException("Escopo inválido: " + codigo);
    }
    
    // Método para compatibilidade com valores do Postman
    @JsonCreator
    public static EscopoEnum fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do escopo não pode ser nulo");
        }
        
        // Compatibilidade com valores do Postman
        switch (valor.toUpperCase()) {
            case "ESCOPO1":
                return ESCOPO1;
            case "ESCOPO2":
                return ESCOPO2;
            case "ESCOPO3":
                return ESCOPO3;
            case "ESCOPO3_PRODUCAO":
                return ESCOPO3_PRODUCAO;
            case "ESCOPO3_TRANSPORTE":
                return ESCOPO3_TRANSPORTE;
            default:
                // Tenta buscar pelo código
                return fromCodigo(valor.toLowerCase());
        }
    }
}