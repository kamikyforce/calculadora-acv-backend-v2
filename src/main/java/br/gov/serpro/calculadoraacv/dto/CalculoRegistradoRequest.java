package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculoRegistradoRequest {
    
    @NotBlank(message = "CAR é obrigatório")
    @Size(max = 50, message = "CAR deve ter no máximo 50 caracteres")
    @Pattern(regexp = "^[A-Z]{2}-\\d{7}-[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}\\.[A-F0-9]{4}$", 
             message = "CAR deve estar no formato UF-1234567-E6D3.395B.6D27.4F42.AE22.DD56.987C.DD52")
    private String car;
    
    @NotBlank(message = "Fazenda é obrigatória")
    @Size(max = 100, message = "Nome da fazenda deve ter no máximo 100 caracteres")
    private String fazenda;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Size(max = 50, message = "Tipo deve ter no máximo 50 caracteres")
    private String tipo;
    
    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;
    
    @Size(max = 100, message = "Município deve ter no máximo 100 caracteres")
    private String municipio;
    
    private BigDecimal tamanho;
    
    @NotNull(message = "Ano é obrigatório")
    @Min(value = 2000, message = "Ano deve ser maior que 2000")
    private Integer ano;
    
    @NotBlank(message = "Versão é obrigatória")
    @Size(max = 10, message = "Versão deve ter no máximo 10 caracteres")
    private String versao;
    
    @NotNull(message = "Status é obrigatório")
    private StatusCalculoRegistrado status;
    
    private BigDecimal emissaoTotal;
    
    @NotNull(message = "Certificação é obrigatória")
    private TipoCertificacao certificacao;
}