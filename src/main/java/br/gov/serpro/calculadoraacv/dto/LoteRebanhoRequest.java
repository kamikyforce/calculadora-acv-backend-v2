package br.gov.serpro.calculadoraacv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class LoteRebanhoRequest {

    @NotNull(message = "ID do inventário é obrigatório")
    private Long inventarioId;

    @NotBlank(message = "Nome do lote é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    private Integer ordem;
    private List<CategoriaLoteRequest> categorias;
    private NutricaoAnimalLoteRequest nutricaoAnimal;
    private List<ManejoDejetosLoteRequest> manejosDejetos;

    // Getters and Setters
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

    public List<CategoriaLoteRequest> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaLoteRequest> categorias) {
        this.categorias = categorias;
    }

    public NutricaoAnimalLoteRequest getNutricaoAnimal() {
        return nutricaoAnimal;
    }

    public void setNutricaoAnimal(NutricaoAnimalLoteRequest nutricaoAnimal) {
        this.nutricaoAnimal = nutricaoAnimal;
    }

    public List<ManejoDejetosLoteRequest> getManejosDejetos() {
        return manejosDejetos;
    }

    public void setManejosDejetos(List<ManejoDejetosLoteRequest> manejosDejetos) {
        this.manejosDejetos = manejosDejetos;
    }
}