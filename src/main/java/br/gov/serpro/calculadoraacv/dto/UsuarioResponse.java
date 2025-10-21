package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioResponse {
    private String id;  // Changed to String to match frontend
    private String nome;
    private String email;
    private String cpf;
    private TipoUsuario tipo;
    private List<String> perfis;  // Added for frontend compatibility
    private boolean ativo;
    private String dataCadastro;
}