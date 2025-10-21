package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredienteResponse {
    
    private Long id;
    private String nome;
    private String tipo;
    private String fonte; // bd_dieta_ingredientes, bd_valor, bd_fator_emissao_dieta_ndt_ingredientes
    
    // Dados nutricionais comuns
    private BigDecimal ndtPercentual;
    private BigDecimal energiaBruta;
    private BigDecimal materiaSeca;
    private BigDecimal proteinaBruta;
    private BigDecimal fibraDetrgenteNeutro;
    
    // Dados específicos do bd_valor
    private String representatividadeCorte;
    private String representatividadeLeite;
    private BigDecimal extratoEtereo;
    private BigDecimal materiaMineral;
    
    // Dados específicos do bd_fator_emissao_dieta_ndt_ingredientes
    private String fatoresEmissoesCalculados;
    private String observacoes;
    
    public IngredienteResponse() {}
    
    public IngredienteResponse(Long id, String nome, String tipo, String fonte) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.fonte = fonte;
    }
}