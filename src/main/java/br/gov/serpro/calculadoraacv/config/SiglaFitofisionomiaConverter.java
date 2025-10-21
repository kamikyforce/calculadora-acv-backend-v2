package br.gov.serpro.calculadoraacv.config;

import br.gov.serpro.calculadoraacv.enums.SiglaFitofisionomia;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class SiglaFitofisionomiaConverter implements AttributeConverter<SiglaFitofisionomia, String> {

    @Override
    public String convertToDatabaseColumn(SiglaFitofisionomia attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public SiglaFitofisionomia convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            return SiglaFitofisionomia.fromCodigo(dbData.trim());
        } catch (IllegalArgumentException e) {
            log.warn("Sigla Fitofisionomia inválida '{}', usando valor padrão FOD. Erro: {}", 
                dbData, e.getMessage());
            return SiglaFitofisionomia.FOD;
        }
    }
}