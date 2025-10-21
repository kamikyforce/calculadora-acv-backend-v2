package br.gov.serpro.calculadoraacv.dto;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutStats {
    
    private Long totalFatores;
    private Long fatoresAtivos;
    private Long fatoresInativos;
    private Map<String, Long> fatoresPorTipo;
    private Map<String, Long> fatoresPorEscopo;
    private Map<String, Long> fatoresPorBioma;
    private Map<String, Long> fatoresPorUsuario;
}