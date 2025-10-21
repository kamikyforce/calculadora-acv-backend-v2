package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculos_registrados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoRegistrado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "car", nullable = false, length = 50)
    private String car;
    
    @Column(name = "fazenda", nullable = false, length = 100)
    private String fazenda;
    
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;
    
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;
    
    @Column(name = "municipio", length = 100)
    private String municipio;
    
    @Column(name = "tamanho", precision = 15, scale = 6)
    private BigDecimal tamanho;
    
    @Column(name = "ano", nullable = false)
    private Integer ano;
    
    @Column(name = "versao", nullable = false, length = 10)
    private String versao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusCalculoRegistrado status;
    
    @Column(name = "emissao_total", precision = 15, scale = 3)
    private BigDecimal emissaoTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "certificacao", nullable = false)
    private TipoCertificacao certificacao;
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}