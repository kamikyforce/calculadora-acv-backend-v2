package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.InventarioJornada;
import java.time.LocalDateTime;
import java.util.List;

public class InventarioJornadaResponse {

    private Long id;
    private Long usuarioId;
    private String nome;
    private InventarioJornada.TipoRebanho tipoRebanho;
    private InventarioJornada.StatusInventario status;
    private Integer faseAtual;
    private Boolean faseRebanhoConcluida;
    private Boolean faseProducaoAgricolaConcluida;
    private Boolean faseMutConcluida;
    private Boolean faseEnergiaConcluida;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<LoteRebanhoResponse> lotes;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

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

    public List<LoteRebanhoResponse> getLotes() {
        return lotes;
    }

    public void setLotes(List<LoteRebanhoResponse> lotes) {
        this.lotes = lotes;
    }
}