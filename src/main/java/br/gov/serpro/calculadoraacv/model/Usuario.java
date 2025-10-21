package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.OrigemAutenticacao;
import br.gov.serpro.calculadoraacv.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_autenticacao")
    private OrigemAutenticacao origemAutenticacao = OrigemAutenticacao.GOVBR;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
}