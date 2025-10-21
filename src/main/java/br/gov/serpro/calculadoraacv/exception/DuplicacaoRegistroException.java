package br.gov.serpro.calculadoraacv.exception;

public class DuplicacaoRegistroException extends RuntimeException {
    private String codigo;
    
    public DuplicacaoRegistroException(String message) {
        super(message);
        this.codigo = "DUPLICACAO_REGISTRO";
    }
    
    public DuplicacaoRegistroException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    // Métodos específicos para diferentes tipos de duplicação
    public static DuplicacaoRegistroException anoJaCadastrado(String tipoEnergia, String escopo, int ano) {
        return new DuplicacaoRegistroException(
            String.format("Já existe um registro de %s no %s para o ano %d. Para editar os dados existentes, utilize a opção 'Editar' na listagem.", 
                tipoEnergia.toLowerCase(), escopo.toLowerCase(), ano),
            "ANO_JA_CADASTRADO"
        );
    }
    
    public static DuplicacaoRegistroException dadoMensalExistente(int ano, String mes) {
        return new DuplicacaoRegistroException(
            String.format("Já existe um dado mensal cadastrado para %s de %d. Para alterar, utilize a opção de edição.", mes, ano),
            "DADO_MENSAL_EXISTENTE"
        );
    }
    
    public static DuplicacaoRegistroException dadoConsolidadoExistente(int ano) {
        return new DuplicacaoRegistroException(
            String.format("Já existe um dado consolidado cadastrado para o ano %d. Para alterar, utilize a opção de edição.", ano),
            "DADO_CONSOLIDADO_EXISTENTE"
        );
    }
}