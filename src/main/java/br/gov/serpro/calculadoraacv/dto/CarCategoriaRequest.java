package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CarCategoriaRequest {

    @NotNull(message = "ID da categoria do lote é obrigatório")
    private Long categoriaLoteId;

    @NotBlank(message = "Número do CAR é obrigatório")
    @Size(max = 50, message = "Número do CAR deve ter no máximo 50 caracteres")
    private String numeroCar;

    @NotNull(message = "Número de animais comprados é obrigatório")
    @Min(value = 0, message = "Número de animais comprados deve ser maior ou igual a 0")
    private Integer animaisComprados;

    private Integer ordem;

    // Getters and Setters
    public Long getCategoriaLoteId() {
        return categoriaLoteId;
    }

    public void setCategoriaLoteId(Long categoriaLoteId) {
        this.categoriaLoteId = categoriaLoteId;
    }

    public String getNumeroCar() {
        return numeroCar;
    }

    public void setNumeroCar(String numeroCar) {
        this.numeroCar = numeroCar;
    }

    public Integer getAnimaisComprados() {
        return animaisComprados;
    }

    public void setAnimaisComprados(Integer animaisComprados) {
        this.animaisComprados = animaisComprados;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}