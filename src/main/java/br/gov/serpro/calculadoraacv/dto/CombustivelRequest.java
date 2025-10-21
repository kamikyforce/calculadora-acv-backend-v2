package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CombustivelRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Size(max = 50, message = "Tipo deve ter no máximo 50 caracteres")
    private String tipo;
    
    @NotNull(message = "Fator de emissão CO2 é obrigatório")
    @DecimalMin(value = "0.0", message = "Fator de emissão CO2 deve ser positivo")
    private BigDecimal fatorEmissaoCO2;
    
    @NotNull(message = "Fator de emissão CH4 é obrigatório")
    @DecimalMin(value = "0.0", message = "Fator de emissão CH4 deve ser positivo")
    private BigDecimal fatorEmissaoCH4;
    
    @NotNull(message = "Fator de emissão N2O é obrigatório")
    @DecimalMin(value = "0.0", message = "Fator de emissão N2O deve ser positivo")
    private BigDecimal fatorEmissaoN2O;
    
    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unidade;
    
    // NEW FIELD FOR SCOPE SUPPORT
    private EscopoEnum escopo;
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getFatorEmissaoCO2() { return fatorEmissaoCO2; }
    public void setFatorEmissaoCO2(BigDecimal fatorEmissaoCO2) { this.fatorEmissaoCO2 = fatorEmissaoCO2; }
    
    public BigDecimal getFatorEmissaoCH4() { return fatorEmissaoCH4; }
    public void setFatorEmissaoCH4(BigDecimal fatorEmissaoCH4) { this.fatorEmissaoCH4 = fatorEmissaoCH4; }
    
    public BigDecimal getFatorEmissaoN2O() { return fatorEmissaoN2O; }
    public void setFatorEmissaoN2O(BigDecimal fatorEmissaoN2O) { this.fatorEmissaoN2O = fatorEmissaoN2O; }
    
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    
    // NEW GETTER/SETTER FOR SCOPE
    public EscopoEnum getEscopo() { return escopo; }
    public void setEscopo(EscopoEnum escopo) { this.escopo = escopo; }
}