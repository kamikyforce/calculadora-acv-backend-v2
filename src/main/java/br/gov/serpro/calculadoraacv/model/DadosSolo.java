package br.gov.serpro.calculadoraacv.model;

import br.gov.serpro.calculadoraacv.enums.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dados_solo")
public class DadosSolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fator_mut_id")
    private FatorMut fatorMut;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_fator_solo", nullable = false)
    private TipoFatorSolo tipoFatorSolo;

    @Column(name = "valor_fator", precision = 15, scale = 6, nullable = false)
    private BigDecimal valorFator;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "bioma")
    private Bioma bioma;

    @Column(name = "fator_co2", precision = 15, scale = 6)
    private BigDecimal fatorCO2;

    @Column(name = "fator_ch4", precision = 15, scale = 6)
    private BigDecimal fatorCH4;

    @Column(name = "fator_n2o", precision = 15, scale = 6)
    private BigDecimal fatorN2O;

    @Column(name = "uso_anterior", length = 255)
    private String usoAnterior;

    @Column(name = "uso_atual", length = 255)
    private String usoAtual;

    @Column(name = "principal", nullable = false)
    private Boolean principal = Boolean.TRUE;

    @Column(name = "replicado_automatico", nullable = false)
    private Boolean replicadoAutomatico = Boolean.FALSE;

    // Getters/Setters explícitos (evita problemas com Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FatorMut getFatorMut() { return fatorMut; }
    public void setFatorMut(FatorMut fatorMut) { this.fatorMut = fatorMut; }

    public TipoFatorSolo getTipoFatorSolo() { return tipoFatorSolo; }
    public void setTipoFatorSolo(TipoFatorSolo tipoFatorSolo) { this.tipoFatorSolo = tipoFatorSolo; }

    public BigDecimal getValorFator() { return valorFator; }
    public void setValorFator(BigDecimal valorFator) { this.valorFator = valorFator; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Bioma getBioma() { return bioma; }
    public void setBioma(Bioma bioma) { this.bioma = bioma; }

    public BigDecimal getFatorCO2() { return fatorCO2; }
    public void setFatorCO2(BigDecimal fatorCO2) { this.fatorCO2 = fatorCO2; }

    public BigDecimal getFatorCH4() { return fatorCH4; }
    public void setFatorCH4(BigDecimal fatorCH4) { this.fatorCH4 = fatorCH4; }

    public BigDecimal getFatorN2O() { return fatorN2O; }
    public void setFatorN2O(BigDecimal fatorN2O) { this.fatorN2O = fatorN2O; }

    public String getUsoAnterior() { return usoAnterior; }
    public void setUsoAnterior(String usoAnterior) { this.usoAnterior = usoAnterior; }

    public String getUsoAtual() { return usoAtual; }
    public void setUsoAtual(String usoAtual) { this.usoAtual = usoAtual; }

    public Boolean getPrincipal() { return principal; }
    public void setPrincipal(Boolean principal) { this.principal = principal; }

    public Boolean getReplicadoAutomatico() { return replicadoAutomatico; }
    public void setReplicadoAutomatico(Boolean replicadoAutomatico) { this.replicadoAutomatico = replicadoAutomatico; }
}