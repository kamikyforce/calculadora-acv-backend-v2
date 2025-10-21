package br.gov.serpro.calculadoraacv.exception;

public class TipoDadoInvalidoException extends RuntimeException {
    private String codigo;
    
    public TipoDadoInvalidoException(String message) {
        super(message);
        this.codigo = "TIPO_DADO_INVALIDO";
    }
    
    public TipoDadoInvalidoException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public static TipoDadoInvalidoException conversaoInvalida(String tipoOrigem, String tipoDestino) {
        return new TipoDadoInvalidoException(
            String.format("Não é possível converter dados do tipo %s para %s. Para alterar o tipo de dado, crie um novo registro.", 
                tipoOrigem, tipoDestino),
            "CONVERSAO_TIPO_INVALIDA"
        );
    }
    
    public static TipoDadoInvalidoException dadosMensaisIncompletos(int mesesPreenchidos) {
        return new TipoDadoInvalidoException(
            String.format("Para calcular a média anual, é necessário preencher todos os 12 meses. Atualmente preenchidos: %d meses", 
                mesesPreenchidos),
            "DADOS_MENSAIS_INCOMPLETOS"
        );
    }
    
    public static TipoDadoInvalidoException anoNaoEditavel() {
        return new TipoDadoInvalidoException(
            "O ano de referência não pode ser alterado após o cadastro. Para usar um ano diferente, crie um novo registro.",
            "ANO_NAO_EDITAVEL"
        );
    }
}