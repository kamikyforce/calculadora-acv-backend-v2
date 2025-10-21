package br.gov.serpro.calculadoraacv.enums;

public enum ClasseProducaoAgricola {
    NITROGENADOS("Nitrogenados"),
    FOSFATADOS("Fosfatados"),
    POTASSICOS("Potássicos"),
    CALCARIOS("Calcários"),
    ORGANICOS("Orgânicos"),
    MICRONUTRIENTES("Micronutrientes"),
    HERBICIDAS("Herbicidas"),
    INSETICIDAS("Inseticidas"),
    FUNGICIDAS("Fungicidas"),
    ACARICIDAS("Acaricidas"),
    CEREAIS("Cereais"),
    LEGUMINOSAS("Leguminosas"),
    OLEAGINOSAS("Oleaginosas"),
    FORRAGEIRAS("Forrageiras"),
    DIESEL("Diesel"),
    GASOLINA("Gasolina"),
    ETANOL("Etanol"),
    ELETRICA("Elétrica"),
    SOLAR("Solar"),
    EOLICA("Eólica"),
    OUTRO("Outro");
    
    private final String descricao;
    
    ClasseProducaoAgricola(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static ClasseProducaoAgricola fromDescricao(String descricao) {
        for (ClasseProducaoAgricola classe : values()) {
            if (classe.getDescricao().equals(descricao)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Classe de produção agrícola inválida: " + descricao);
    }
}