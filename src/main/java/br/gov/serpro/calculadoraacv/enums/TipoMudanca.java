package br.gov.serpro.calculadoraacv.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum TipoMudanca {
    DESMATAMENTO("desmatamento", "Desmatamento"),
    VEGETACAO("vegetacao", "Vegetação"),
    SOLO("solo", "Solo");
    
    private final String codigo;
    private final String descricao;
    
    TipoMudanca(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoMudanca fromCodigo(String codigo) {
        for (TipoMudanca tipo : values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de mudança inválido: " + codigo);
    }
    
    // Método para compatibilidade com valores do Postman
    @JsonCreator
    public static TipoMudanca fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do tipo de mudança não pode ser nulo");
        }
        
        switch (valor.toUpperCase()) {
            case "DESMATAMENTO":
                return DESMATAMENTO;
            case "VEGETACAO":
            case "VEGETAÇÃO":
                return VEGETACAO;
            case "SOLO":
                return SOLO;
            default:
                return fromCodigo(valor.toLowerCase());
        }
    }
}