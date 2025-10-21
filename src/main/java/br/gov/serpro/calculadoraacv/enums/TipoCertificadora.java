package br.gov.serpro.calculadoraacv.enums;

import br.gov.serpro.calculadoraacv.config.TipoCertificadoraDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TipoCertificadoraDeserializer.class)
public enum TipoCertificadora {
    FRIGORIFICO, LATICINIO, AMBOS
}
