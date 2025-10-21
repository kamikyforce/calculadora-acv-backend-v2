package br.gov.serpro.calculadoraacv.exception;

import java.math.BigDecimal;

public class PrecisaoDecimalException extends RuntimeException {
    private String codigo;
    
    public PrecisaoDecimalException(String message) {
        super(message);
        this.codigo = "PRECISAO_DECIMAL_INVALIDA";
    }
    
    public PrecisaoDecimalException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public static PrecisaoDecimalException valorExcedePrecisao(BigDecimal valor, int precisaoMaxima) {
        return new PrecisaoDecimalException(
            String.format("O valor %s excede a precisão máxima permitida de %d casas decimais", 
                valor.toString(), precisaoMaxima),
            "VALOR_EXCEDE_PRECISAO"
        );
    }
    
    public static PrecisaoDecimalException fatorMedioInvalido(BigDecimal fator) {
        return new PrecisaoDecimalException(
            String.format("O fator médio anual deve ter no máximo 6 casas decimais. Valor informado: %s", 
                fator.toString()),
            "FATOR_MEDIO_PRECISAO_INVALIDA"
        );
    }
}