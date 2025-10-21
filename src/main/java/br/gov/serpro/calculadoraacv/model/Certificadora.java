package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.TipoCertificadora;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certificadora")
public class Certificadora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false, length = 18)
    private String cnpj;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "inventarios_tratados")
    private int inventariosTratados = 0;

    @Column(length = 2, nullable = false)
    private String estado;

    @Enumerated(EnumType.STRING)
    private TipoCertificadora tipo;

    private boolean ativo;

    @OneToMany(mappedBy = "certificadora", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioCertificadora> usuarios = new ArrayList<>();
}