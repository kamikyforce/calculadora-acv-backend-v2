package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.TipoIndustria;
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
@Table(name = "industria")
public class Industria {
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
    private TipoIndustria tipo = TipoIndustria.INDUSTRIA;

    private boolean ativo = true;

    @OneToMany(mappedBy = "industria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioIndustria> usuarios = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }
}