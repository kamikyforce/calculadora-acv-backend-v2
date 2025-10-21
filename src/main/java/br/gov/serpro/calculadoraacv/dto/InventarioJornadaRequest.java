package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.InventarioJornada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InventarioJornadaRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @NotNull(message = "Tipo de rebanho é obrigatório")
    private InventarioJornada.TipoRebanho tipoRebanho;

    private InventarioJornada.StatusInventario status;
    private Integer faseAtual;
    private Boolean faseRebanhoConcluida;
    private Boolean faseProducaoAgricolaConcluida;
    private Boolean faseMutConcluida;
    private Boolean faseEnergiaConcluida;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public InventarioJornada.TipoRebanho getTipoRebanho() {
        return tipoRebanho;
    }

    public void setTipoRebanho(InventarioJornada.TipoRebanho tipoRebanho) {
        this.tipoRebanho = tipoRebanho;
    }

    public InventarioJornada.StatusInventario getStatus() {
        return status;
    }

    public void setStatus(InventarioJornada.StatusInventario status) {
        this.status = status;
    }

    public Integer getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(Integer faseAtual) {
        this.faseAtual = faseAtual;
    }

    public Boolean getFaseRebanhoConcluida() {
        return faseRebanhoConcluida;
    }

    public void setFaseRebanhoConcluida(Boolean faseRebanhoConcluida) {
        this.faseRebanhoConcluida = faseRebanhoConcluida;
    }

    public Boolean getFaseProducaoAgricolaConcluida() {
        return faseProducaoAgricolaConcluida;
    }

    public void setFaseProducaoAgricolaConcluida(Boolean faseProducaoAgricolaConcluida) {
        this.faseProducaoAgricolaConcluida = faseProducaoAgricolaConcluida;
    }

    public Boolean getFaseMutConcluida() {
        return faseMutConcluida;
    }

    public void setFaseMutConcluida(Boolean faseMutConcluida) {
        this.faseMutConcluida = faseMutConcluida;
    }

    public Boolean getFaseEnergiaConcluida() {
        return faseEnergiaConcluida;
    }

    public void setFaseEnergiaConcluida(Boolean faseEnergiaConcluida) {
        this.faseEnergiaConcluida = faseEnergiaConcluida;
    }
}