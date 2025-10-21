package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import br.gov.serpro.calculadoraacv.model.CalculoRegistrado;
import lombok.Data;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
public class CalculoRegistradoResponse {
    
    private Long id;
    private String car;
    private String fazenda;
    private String tipo;
    private String estado;
    private String municipio;
    private BigDecimal tamanho;
    private Integer ano;
    private String versao;
    private String status;
    private BigDecimal emissaoTotal;
    private String certificacao;
    private String dataCriacao;
    private String dataAtualizacao;
    
    public CalculoRegistradoResponse(CalculoRegistrado entidade) {
        this.id = entidade.getId();
        this.car = entidade.getCar();
        this.fazenda = entidade.getFazenda();
        this.tipo = entidade.getTipo();
        this.estado = entidade.getEstado();
        this.municipio = entidade.getMunicipio();
        this.tamanho = entidade.getTamanho();
        this.ano = entidade.getAno();
        this.versao = entidade.getVersao();
        this.status = entidade.getStatus().getDescricao();
        this.emissaoTotal = entidade.getEmissaoTotal();
        this.certificacao = entidade.getCertificacao().getDescricao();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        if (entidade.getDataCriacao() != null) {
            this.dataCriacao = entidade.getDataCriacao().format(formatter);
        }
        
        if (entidade.getDataAtualizacao() != null) {
            this.dataAtualizacao = entidade.getDataAtualizacao().format(formatter);
        }
    }
}