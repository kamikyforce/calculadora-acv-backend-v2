package br.gov.serpro.calculadoraacv.exception;

public class RegraDeNegocioException extends RuntimeException {
    private String codigo;
    private String campo;
    
    public RegraDeNegocioException(String message) {
        super(message);
        this.codigo = "REGRA_NEGOCIO_VIOLADA";
    }
    
    public RegraDeNegocioException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public RegraDeNegocioException(String message, String codigo, String campo) {
        super(message);
        this.codigo = codigo;
        this.campo = campo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getCampo() {
        return campo;
    }
    
    public static RegraDeNegocioException anoObrigatorio() {
        return new RegraDeNegocioException(
            "O ano de referência é obrigatório e deve ser informado manualmente",
            "ANO_OBRIGATORIO",
            "anoReferencia"
        );
    }
    
    public static RegraDeNegocioException anoInvalido(int ano) {
        return new RegraDeNegocioException(
            String.format("O ano %d não é válido. Informe um ano entre 1990 e 2050", ano),
            "ANO_INVALIDO",
            "anoReferencia"
        );
    }
    
    public static RegraDeNegocioException valorNegativo(String campo) {
        return new RegraDeNegocioException(
            String.format("O campo %s não pode ter valor negativo", campo),
            "VALOR_NEGATIVO",
            campo
        );
    }
}