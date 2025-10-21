package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.TipoIndustria;
import br.gov.serpro.calculadoraacv.model.Industria;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class IndustriaResponse {
    private Long id;
    private String nome;
    private String cnpj;
    private String dataCadastro;
    private int inventariosTratados;
    private String estado;
    private TipoIndustria tipo;
    private boolean ativo;

    public IndustriaResponse(Industria industria) {
        this.id = industria.getId();
        this.nome = industria.getNome();
        this.cnpj = industria.getCnpj();
        if (industria.getDataCadastro() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.dataCadastro = industria.getDataCadastro().format(formatter);
        }
        this.inventariosTratados = industria.getInventariosTratados();
        this.estado = industria.getEstado();
        this.tipo = industria.getTipo();
        this.ativo = industria.isAtivo();
    }
}
