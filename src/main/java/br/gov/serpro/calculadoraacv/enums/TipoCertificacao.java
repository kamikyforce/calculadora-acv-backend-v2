package br.gov.serpro.calculadoraacv.enums;

public enum TipoCertificacao {
    CERTIFICADO("CERTIFICADO", "Certificado"),
    EM_CERTIFICACAO("EM_CERTIFICACAO", "Em certificação"),
    NAO_INICIADO("NAO_INICIADO", "Não iniciado"),
    NAO_CERTIFICADO("NAO_CERTIFICADO", "Não certificado");
    
    private final String codigo;
    private final String descricao;
    
    TipoCertificacao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() { 
        return codigo; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public static TipoCertificacao fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da certificação não pode ser nulo");
        }
        
        switch (valor.toUpperCase()) {
            case "CERTIFICADO":
                return CERTIFICADO;
            case "EM CERTIFICAÇÃO":
            case "EM_CERTIFICACAO":
                return EM_CERTIFICACAO;
            case "NÃO INICIADO":
            case "NAO_INICIADO":
                return NAO_INICIADO;
            case "NÃO CERTIFICADO":
            case "NAO_CERTIFICADO":
                return NAO_CERTIFICADO;
            default:
                throw new IllegalArgumentException("Tipo de certificação inválido: " + valor);
        }
    }
}