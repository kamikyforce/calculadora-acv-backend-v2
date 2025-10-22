package br.gov.bndes.calculadoraacv.mut.exception;

public class DadosDuplicadosException extends RuntimeException {
    private final Long idExistente;
    private final String escopo;
    private final String tipoFatorSolo;
    private final String usoAnterior;
    private final String usoAtual;

    public DadosDuplicadosException(Long idExistente, String escopo, String tipoFatorSolo, String usoAnterior, String usoAtual) {
        super("Dados duplicados detectados. Verifique se o registro já existe.");
        this.idExistente = idExistente;
        this.escopo = escopo;
        this.tipoFatorSolo = tipoFatorSolo;
        this.usoAnterior = usoAnterior;
        this.usoAtual = usoAtual;
    }

    public Long getIdExistente() { return idExistente; }
    public String getEscopo() { return escopo; }
    public String getTipoFatorSolo() { return tipoFatorSolo; }
    public String getUsoAnterior() { return usoAnterior; }
    public String getUsoAtual() { return usoAtual; }
}