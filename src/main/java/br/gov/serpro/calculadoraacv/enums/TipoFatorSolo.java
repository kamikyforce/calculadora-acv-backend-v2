package br.gov.serpro.calculadoraacv.enums;

public enum TipoFatorSolo {
    USO_ANTERIOR_ATUAL("uso-anterior-atual", "Fatores de emissão que variam com o uso anterior e atual"),
    SOLO_USO_ANTERIOR_ATUAL("solo-uso-anterior-atual", "Fatores de emissão que variam com o tipo de solo e o uso anterior e atual");

    private final String codigo;
    private final String descricao;

    TipoFatorSolo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoFatorSolo fromCodigo(String codigo) {
        for (TipoFatorSolo tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de fator solo inválido: " + codigo);
    }

    public static TipoFatorSolo fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor não pode ser nulo ou vazio");
        }
        
        return switch (valor.trim().toUpperCase()) {
            case "USO_ANTERIOR_ATUAL", "SOLO_LAC", "ALTO_CARBONO", "BAIXO_CARBONO" -> USO_ANTERIOR_ATUAL;
            case "SOLO_USO_ANTERIOR_ATUAL", "SOLO_ARENOSO", "MINERAL", "ORGANICO" -> SOLO_USO_ANTERIOR_ATUAL;
            default -> throw new IllegalArgumentException("Valor inválido para TipoFatorSolo: " + valor);
        };
    }
}