package br.gov.serpro.calculadoraacv.dto;

import java.time.LocalDateTime;

public class CarCategoriaResponse {

    private Long id;
    private Long categoriaLoteId;
    private String numeroCar;
    private Integer animaisComprados;
    private Integer ordem;
    private LocalDateTime dataCriacao;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}