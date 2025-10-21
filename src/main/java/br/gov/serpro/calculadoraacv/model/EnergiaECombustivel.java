package br.gov.serpro.calculadoraacv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import br.gov.serpro.calculadoraacv.enums.StatusCalculo;
import br.gov.serpro.calculadoraacv.enums.TipoDado;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "energia_dados")
public class EnergiaECombustivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo_energia", nullable = false, length = 50)
    private String tipoEnergia;

    @Column(name = "fonte_energia", length = 100)
    private String fonteEnergia;

    @Column(name = "consumo_anual", precision = 15, scale = 3)
    private BigDecimal consumoAnual;

    @Column(length = 20)
    private String unidade;

    @Column(name = "fator_emissao", precision = 10, scale = 6)
    private BigDecimal fatorEmissao;

    // NOVO CAMPO DE ESCOPO
    @Column(name = "escopo", length = 50)
    private String escopo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false)
    private Boolean ativo = true;

    // NOVO CAMPO DE ANO DE REFERÊNCIA
    @Column(name = "ano_referencia")
    private Integer anoReferencia;

    // NOVO CAMPO PARA DADOS MENSAIS
    @Column(name = "dados_mensais", columnDefinition = "TEXT")
    private String dadosMensais;

    // NOVOS CAMPOS PARA DADO CONSOLIDADO DO ANO E DADO MENSAL
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dado", length = 20)
    private TipoDado tipoDado = TipoDado.CONSOLIDADO_ANUAL;

    @Column(name = "versao")
    private Integer versao = 1;

    @Column(name = "dados_mensais_json", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String dadosMensaisJson;

    @Column(name = "media_anual_calculada", precision = 15, scale = 3)
    private BigDecimal mediaAnualCalculada;

    @Column(name = "meses_preenchidos")
    private Integer mesesPreenchidos = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_calculo", length = 20)
    private StatusCalculo statusCalculo = StatusCalculo.PENDENTE;

    @Column(name = "usuario_ultima_edicao")
    private Long usuarioUltimaEdicao;

    @Column(name = "observacoes_auditoria", columnDefinition = "TEXT")
    private String observacoesAuditoria;

    // Adicionar após linha 32 (após fatorEmissao)
    @Column(name = "fator_medio_anual", precision = 10, scale = 6)
    private BigDecimal fatorMedioAnual;

    // Construtores
    public EnergiaECombustivel() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public EnergiaECombustivel(Long usuarioId, String tipoEnergia, String fonteEnergia,
            BigDecimal consumoAnual, String unidade, BigDecimal fatorEmissao, String escopo) {
        this();
        this.usuarioId = usuarioId;
        this.tipoEnergia = tipoEnergia;
        this.fonteEnergia = fonteEnergia;
        this.consumoAnual = consumoAnual;
        this.unidade = unidade;
        this.fatorEmissao = fatorEmissao;
        this.escopo = escopo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipoEnergia() {
        return tipoEnergia;
    }

    public void setTipoEnergia(String tipoEnergia) {
        this.tipoEnergia = tipoEnergia;
    }

    public String getFonteEnergia() {
        return fonteEnergia;
    }

    public void setFonteEnergia(String fonteEnergia) {
        this.fonteEnergia = fonteEnergia;
    }

    public BigDecimal getConsumoAnual() {
        return consumoAnual;
    }

    public void setConsumoAnual(BigDecimal consumoAnual) {
        this.consumoAnual = consumoAnual;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    // Adicionar getter e setter
    public BigDecimal getFatorMedioAnual() {
        return fatorMedioAnual;
    }

    public void setFatorMedioAnual(BigDecimal fatorMedioAnual) {
        this.fatorMedioAnual = fatorMedioAnual;
    }

    public BigDecimal getFatorEmissao() {
        return fatorEmissao;
    }

    public void setFatorEmissao(BigDecimal fatorEmissao) {
        this.fatorEmissao = fatorEmissao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // NOVO GETTER E SETTER PARA ESCOPO
    public String getEscopo() {
        return escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    // Getter e Setter para anoReferencia
    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    public void setAnoReferencia(Integer anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    // GETTER E SETTER PARA DADOS MENSAIS
    public String getDadosMensais() {
        return dadosMensais;
    }

    public void setDadosMensais(String dadosMensais) {
        this.dadosMensais = dadosMensais;
    }

    // GETTERS E SETTERS PARA OS NOVOS CAMPOS
    public TipoDado getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDado tipoDado) {
        this.tipoDado = tipoDado;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getDadosMensaisJson() {
        return dadosMensaisJson;
    }

    public void setDadosMensaisJson(String dadosMensaisJson) {
        this.dadosMensaisJson = dadosMensaisJson;
    }

    public BigDecimal getMediaAnualCalculada() {
        return mediaAnualCalculada;
    }

    public void setMediaAnualCalculada(BigDecimal mediaAnualCalculada) {
        this.mediaAnualCalculada = mediaAnualCalculada;
    }

    public Integer getMesesPreenchidos() {
        return mesesPreenchidos;
    }

    public void setMesesPreenchidos(Integer mesesPreenchidos) {
        this.mesesPreenchidos = mesesPreenchidos;
    }

    public StatusCalculo getStatusCalculo() {
        return statusCalculo;
    }

    public void setStatusCalculo(StatusCalculo statusCalculo) {
        this.statusCalculo = statusCalculo;
    }

    public Long getUsuarioUltimaEdicao() {
        return usuarioUltimaEdicao;
    }

    public void setUsuarioUltimaEdicao(Long usuarioUltimaEdicao) {
        this.usuarioUltimaEdicao = usuarioUltimaEdicao;
    }

    public String getObservacoesAuditoria() {
        return observacoesAuditoria;
    }

    public void setObservacoesAuditoria(String observacoesAuditoria) {
        this.observacoesAuditoria = observacoesAuditoria;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
