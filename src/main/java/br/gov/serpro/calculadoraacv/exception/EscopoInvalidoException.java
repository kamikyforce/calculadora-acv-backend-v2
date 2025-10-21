package br.gov.serpro.calculadoraacv.exception;

public class EscopoInvalidoException extends RuntimeException {
    private String codigo;
    
    public EscopoInvalidoException(String message) {
        super(message);
        this.codigo = "ESCOPO_INVALIDO";
    }
    
    public EscopoInvalidoException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    // Métodos específicos para diferentes tipos de energia
    public static EscopoInvalidoException paraEnergia(String escopo) {
        return new EscopoInvalidoException(
            String.format("Para o tipo ENERGIA, apenas o Escopo 2 é permitido. Escopo informado: %s", escopo),
            "ESCOPO_ENERGIA_INVALIDO"
        );
    }
    
    public static EscopoInvalidoException paraCombustivel(String escopo) {
        return new EscopoInvalidoException(
            String.format("Para o tipo COMBUSTÍVEL, apenas os Escopos 1 e 3 são permitidos. Escopo informado: %s", escopo),
            "ESCOPO_COMBUSTIVEL_INVALIDO"
        );
    }
    
    public static EscopoInvalidoException escopoNaoReconhecido(String escopo) {
        return new EscopoInvalidoException(
            String.format("Escopo '%s' não é reconhecido pelo sistema. Escopos válidos: 1, 2, 3", escopo),
            "ESCOPO_NAO_RECONHECIDO"
        );
    }
}