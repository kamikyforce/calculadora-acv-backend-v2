package br.gov.serpro.calculadoraacv.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SiglaFitofisionomia {
    // Valores originais
    FOD("FOD", "Floresta Ombrófila Densa"),
    FOM("FOM", "Floresta Ombrófila Mista"),
    FES("FES", "Floresta Estacional Semidecidual"),
    FED("FED", "Floresta Estacional Decidual"),
    SAV("SAV", "Savana"),
    SAS("SAS", "Savana Estépica"),
    CAM("CAM", "Campinarana"),
    FOR("FOR", "Formações Pioneiras"),
    REF("REF", "Áreas de Tensão Ecológica"),
    
    // Valores das regras de negócio
    AA("Aa", "Aa"), AB("Ab", "Ab"), AS("As", "As"),
    CB("Cb", "Cb"), CS("Cs", "Cs"),
    DA("Da", "Da"), DB("Db", "Db"), DM("Dm", "Dm"), DS("Ds", "Ds"),
    FA("Fa", "Fa"), FB("Fb", "Fb"), FM("Fm", "Fm"), FS("Fs", "Fs"),
    LA("La", "La"), LB("Lb", "Lb"), LD("Ld", "Ld"), LG("Lg", "Lg"),
    PA("Pa", "Pa"), PF("Pf", "Pf"), PM("Pm", "Pm"),
    RM("Rm", "Rm"),
    SA("Sa", "Sa"), SD("Sd", "Sd"), SG("Sg", "Sg"), SP("Sp", "Sp"),
    TA("Ta", "Ta"), TD("Td", "Td"), TG("Tg", "Tg"), TP("Tp", "Tp"),
    AM("Am", "Am"), CA("Ca", "Ca"),
    RL("Rl", "Rl"), RS("Rs", "Rs"),
    SN("SN", "SN"), SO("SO", "SO"), ST("ST", "ST"), TN("TN", "TN"),
    LO("LO", "LO"), ON("ON", "ON"),
    T("T", "T"), S("S", "S"), P("P", "P"), L("L", "L"),
    AR("Ar", "Ar"), CM("Cm", "Cm"), DN("Dn", "Dn"), EG("Eg", "Eg"),
    MA("Ma", "Ma"), ML("Ml", "Ml"), MM("Mm", "Mm"), SM("SM", "SM"),
    D("D", "D"), DL("Dl", "Dl"), E("E", "E"),
    EM("EM", "EM"), EN("EN", "EN"), F("F", "F"), M("M", "M"),
    MS("Ms", "Ms"), NM("NM", "NM"), NP("NP", "NP"),
    OM("OM", "OM"), OP("OP", "OP"),
    EA("Ea", "Ea"), EP("Ep", "Ep");
    
    private final String codigo;
    private final String descricao;
    
    SiglaFitofisionomia(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static SiglaFitofisionomia fromCodigo(String codigo) {
        for (SiglaFitofisionomia sigla : values()) {
            if (sigla.getCodigo().equalsIgnoreCase(codigo)) {
                return sigla;
            }
        }
        throw new IllegalArgumentException("Sigla de fitofisionomia inválida: " + codigo);
    }
    
    @JsonCreator
    public static SiglaFitofisionomia fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da sigla de fitofisionomia não pode ser nulo");
        }
        return fromCodigo(valor);
    }
}