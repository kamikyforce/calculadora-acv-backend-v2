package br.gov.serpro.calculadoraacv.dto;

import br.gov.serpro.calculadoraacv.enums.*;
import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutFiltros {
    
    private String nome;
    private String termoBusca;
    private TipoMudanca tipoMudanca;
    private EscopoEnum escopo;
    private Set<Bioma> biomas;
    private Set<UF> ufs;
    private Boolean ativo;
    private String nomeUsuario;
    private Integer page = 0;
    private Integer size = 10;
    private String sort = "dataCriacao";
    private String direction = "desc";
}