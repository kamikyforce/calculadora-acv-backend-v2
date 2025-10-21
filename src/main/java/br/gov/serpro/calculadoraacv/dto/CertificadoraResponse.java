package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import br.gov.serpro.calculadoraacv.model.Certificadora;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class CertificadoraResponse {
    private Long id;
    private String nome;
    private String cnpj;
    private String estado;
    private TipoCertificadora tipo;
    private Boolean ativo;
    private String dataCadastro;
    private Integer inventariosTratados;

    public CertificadoraResponse(Certificadora entidade) {
        this.id = entidade.getId();
        this.nome = entidade.getNome();
        this.cnpj = entidade.getCnpj();
        this.estado = entidade.getEstado();
        this.tipo = entidade.getTipo();
        this.ativo = entidade.isAtivo();
        this.inventariosTratados = entidade.getInventariosTratados();
        
        if (entidade.getDataCadastro() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.dataCadastro = entidade.getDataCadastro().format(formatter);
        }
    }
}
