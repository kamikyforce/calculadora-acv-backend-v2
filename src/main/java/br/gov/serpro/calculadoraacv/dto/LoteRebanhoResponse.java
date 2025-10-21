package br.gov.serpro.calculadoraacv.dto;

import java.time.LocalDateTime;
import java.util.List;

public class LoteRebanhoResponse {

    private Long id;
    private Long inventarioId;
    private String nome;
    private Integer ordem;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<CategoriaLoteResponse> categorias;
    private NutricaoAnimalLoteResponse nutricaoAnimal;
    private List<ManejoDejetosLoteResponse> manejosDejetos;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<CategoriaLoteResponse> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaLoteResponse> categorias) {
        this.categorias = categorias;
    }

    public NutricaoAnimalLoteResponse getNutricaoAnimal() {
        return nutricaoAnimal;
    }

    public void setNutricaoAnimal(NutricaoAnimalLoteResponse nutricaoAnimal) {
        this.nutricaoAnimal = nutricaoAnimal;
    }

    public List<ManejoDejetosLoteResponse> getManejosDejetos() {
        return manejosDejetos;
    }

    public void setManejosDejetos(List<ManejoDejetosLoteResponse> manejosDejetos) {
        this.manejosDejetos = manejosDejetos;
    }
}