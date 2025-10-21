package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.NutricaoAnimalLote;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class NutricaoAnimalLoteResponse {

    private Long id;
    private Long loteId;
    private Boolean inserirDadosDieta;
    private NutricaoAnimalLote.SistemaProducao sistemaProducao;
    private BigDecimal tempoPastoHorasDia;
    private Integer tempoPastoDiasAno;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<IngredienteDietaLoteResponse> ingredientes;
    private List<ConcentradoDietaLoteResponse> concentrados;
    private List<AditivoDietaLoteResponse> aditivos;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoteId() {
        return loteId;
    }

    public void setLoteId(Long loteId) {
        this.loteId = loteId;
    }

    public Boolean getInserirDadosDieta() {
        return inserirDadosDieta;
    }

    public void setInserirDadosDieta(Boolean inserirDadosDieta) {
        this.inserirDadosDieta = inserirDadosDieta;
    }

    public NutricaoAnimalLote.SistemaProducao getSistemaProducao() {
        return sistemaProducao;
    }

    public void setSistemaProducao(NutricaoAnimalLote.SistemaProducao sistemaProducao) {
        this.sistemaProducao = sistemaProducao;
    }

    public BigDecimal getTempoPastoHorasDia() {
        return tempoPastoHorasDia;
    }

    public void setTempoPastoHorasDia(BigDecimal tempoPastoHorasDia) {
        this.tempoPastoHorasDia = tempoPastoHorasDia;
    }

    public Integer getTempoPastoDiasAno() {
        return tempoPastoDiasAno;
    }

    public void setTempoPastoDiasAno(Integer tempoPastoDiasAno) {
        this.tempoPastoDiasAno = tempoPastoDiasAno;
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

    public List<IngredienteDietaLoteResponse> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteDietaLoteResponse> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<ConcentradoDietaLoteResponse> getConcentrados() {
        return concentrados;
    }

    public void setConcentrados(List<ConcentradoDietaLoteResponse> concentrados) {
        this.concentrados = concentrados;
    }

    public List<AditivoDietaLoteResponse> getAditivos() {
        return aditivos;
    }

    public void setAditivos(List<AditivoDietaLoteResponse> aditivos) {
        this.aditivos = aditivos;
    }
}