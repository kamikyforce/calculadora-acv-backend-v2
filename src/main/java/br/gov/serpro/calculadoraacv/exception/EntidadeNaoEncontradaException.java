package br.gov.serpro.calculadoraacv.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(String.format("%s com ID %d não encontrado", entidade, id));
    }
}
