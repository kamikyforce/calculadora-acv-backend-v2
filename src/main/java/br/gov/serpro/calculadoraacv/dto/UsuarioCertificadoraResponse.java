package br.gov.serpro.calculadoraacv.dto;

import lombok.Data;

@Data
public class UsuarioCertificadoraResponse {
    private String cpf;
    private String nome;
    private String dataCadastro;
    private Boolean ativo;
    
    public UsuarioCertificadoraResponse(String cpf, String nome, String dataCadastro, Boolean ativo) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }
}