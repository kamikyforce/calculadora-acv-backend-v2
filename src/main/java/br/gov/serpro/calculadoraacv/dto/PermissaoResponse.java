package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import lombok.Data;

import java.util.List;

@Data
public class PermissaoResponse {
    private TipoUsuario tipoUsuario;
    private List<String> permissoes;
    private String perfil;
}
