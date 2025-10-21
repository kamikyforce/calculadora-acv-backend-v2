package br.gov.serpro.calculadoraacv.config;

import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class TipoCertificadoraDeserializer extends JsonDeserializer<TipoCertificadora> {
    
    @Override
    public TipoCertificadora deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String value = node.asText();
        
        log.debug("Attempting to deserialize TipoCertificadora from value: {}", value);
        
        // Handle common frontend serialization issues
        if ("[object Object]".equals(value) || value == null || value.trim().isEmpty()) {
            String validValues = Arrays.stream(TipoCertificadora.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            
            log.error("Invalid TipoCertificadora value received: '{}'. Valid values are: {}", value, validValues);
            throw new IllegalArgumentException(
                String.format("Valor inválido para TipoCertificadora: '%s'. Valores válidos são: %s", 
                    value, validValues)
            );
        }
        
        try {
            // Normalize the value (trim and uppercase)
            String normalizedValue = value.trim().toUpperCase();
            return TipoCertificadora.valueOf(normalizedValue);
        } catch (IllegalArgumentException e) {
            String validValues = Arrays.stream(TipoCertificadora.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            
            log.error("Failed to deserialize TipoCertificadora from value: '{}'. Valid values are: {}", value, validValues);
            throw new IllegalArgumentException(
                String.format("Valor inválido para TipoCertificadora: '%s'. Valores válidos são: %s", 
                    value, validValues), e
            );
        }
    }
}