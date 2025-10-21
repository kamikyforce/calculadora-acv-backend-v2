package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.model.NutricaoAnimalLote;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class NutricaoAnimalLoteRequest {

    @NotNull(message = "ID do lote é obrigatório")
    private Long loteId;

    @NotNull(message = "Campo 'inserir dados dieta' é obrigatório")
    private Boolean inserirDadosDieta;

    private NutricaoAnimalLote.SistemaProducao sistemaProducao;

    @DecimalMin(value = "0.0", message = "Tempo de pasto em horas por dia deve ser maior ou igual a 0")
    private BigDecimal tempoPastoHorasDia;

    @Min(value = 0, message = "Tempo de pasto em dias por ano deve ser maior ou igual a 0")
    private Integer tempoPastoDiasAno;

    private List<IngredienteDietaLoteRequest> ingredientes;
    private List<ConcentradoDietaLoteRequest> concentrados;
    private List<AditivoDietaLoteRequest> aditivos;

    // Getters and Setters
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

    public List<IngredienteDietaLoteRequest> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteDietaLoteRequest> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<ConcentradoDietaLoteRequest> getConcentrados() {
        return concentrados;
    }

    public void setConcentrados(List<ConcentradoDietaLoteRequest> concentrados) {
        this.concentrados = concentrados;
    }

    public List<AditivoDietaLoteRequest> getAditivos() {
        return aditivos;
    }

    public void setAditivos(List<AditivoDietaLoteRequest> aditivos) {
        this.aditivos = aditivos;
    }
}