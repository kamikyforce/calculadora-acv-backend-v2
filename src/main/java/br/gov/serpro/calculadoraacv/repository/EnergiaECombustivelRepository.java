package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.EnergiaECombustivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnergiaECombustivelRepository extends JpaRepository<EnergiaECombustivel, Long> {

    List<EnergiaECombustivel> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    List<EnergiaECombustivel> findByTipoEnergiaAndAtivoTrue(String tipoEnergia);

    List<EnergiaECombustivel> findByUsuarioIdAndTipoEnergiaAndAtivoTrue(Long usuarioId, String tipoEnergia);

    // NOVAS CONSULTAS POR ESCOPO
    List<EnergiaECombustivel> findByEscopoAndAtivoTrue(String escopo);

    List<EnergiaECombustivel> findByUsuarioIdAndEscopoAndAtivoTrue(Long usuarioId, String escopo);

    List<EnergiaECombustivel> findByUsuarioIdAndTipoEnergiaAndEscopoAndAtivoTrue(Long usuarioId, String tipoEnergia,
            String escopo);

    @Query("SELECT DISTINCT e.tipoEnergia FROM EnergiaECombustivel e WHERE e.ativo = true ORDER BY e.tipoEnergia")
    List<String> findDistinctTiposEnergia();

    // NOVA CONSULTA PARA LISTAR ESCOPOS DISTINTOS
    @Query("SELECT DISTINCT e.escopo FROM EnergiaECombustivel e WHERE e.ativo = true AND e.escopo IS NOT NULL ORDER BY e.escopo")
    List<String> findDistinctEscopos();

    @Query("SELECT e FROM EnergiaECombustivel e WHERE e.usuarioId = :usuarioId AND e.fonteEnergia LIKE %:fonte% AND e.ativo = true")
    List<EnergiaECombustivel> findByUsuarioIdAndFonteEnergiaContainingAndAtivoTrue(@Param("usuarioId") Long usuarioId,
            @Param("fonte") String fonte);

    List<EnergiaECombustivel> findByAtivoTrue();

    // QUERIES CORRIGIDAS - PRIORIZAM VALOR MANUAL DO USUÁRIO

    // QUERIES CORRIGIDAS - INCLUINDO dados_mensais_json
    @Query(value = "WITH ultimo AS ( " +
            "  SELECT MAX(ano) AS ano FROM banco_fatores_energia WHERE ativo = true " +
            ") " +
            "SELECT e.id, e.usuario_id, e.tipo_energia, e.fonte_energia, " +
            "       e.consumo_anual, e.unidade, e.escopo, " +
            "       COALESCE(e.ano_referencia, b_ultimo.ano) AS ano, " +
            "       COALESCE(e.fator_medio_anual, b.fator_medio_anual, b_ultimo.fator_medio_anual, 0) AS fator_medio_anual, " +
            "       e.data_criacao, e.data_atualizacao, e.ano_referencia, " +
            "       e.dados_mensais_json " + // 👈 ADICIONADO
            "FROM energia_dados e " +
            "LEFT JOIN banco_fatores_energia b ON b.ano = e.ano_referencia " +
            "CROSS JOIN ultimo " +
            "LEFT JOIN banco_fatores_energia b_ultimo ON b_ultimo.ano = ultimo.ano " +
            "WHERE e.ativo = true " +
            "ORDER BY COALESCE(e.ano_referencia, b_ultimo.ano) DESC NULLS LAST", nativeQuery = true)
    List<Object[]> findAllEnergiaComFatorMedio();

    @Query(value = "WITH ultimo AS ( " +
            "  SELECT MAX(ano) AS ano FROM banco_fatores_energia WHERE ativo = true " +
            ") " +
            "SELECT e.id, e.usuario_id, e.tipo_energia, e.fonte_energia, " +
            "       e.consumo_anual, e.unidade, e.escopo, " +
            "       COALESCE(e.ano_referencia, b_ultimo.ano) AS ano, " +
            "       COALESCE(e.fator_medio_anual, b.fator_medio_anual, b_ultimo.fator_medio_anual, 0) AS fator_medio_anual, " +
            "       e.data_criacao, e.data_atualizacao, e.ano_referencia, " +
            "       e.dados_mensais_json, " +
            "       e.media_anual_calculada, " +
            "       e.meses_preenchidos, " +
            "       e.status_calculo " +
            "FROM energia_dados e " +
            "LEFT JOIN banco_fatores_energia b ON b.ano = e.ano_referencia " +
            "CROSS JOIN ultimo " +
            "LEFT JOIN banco_fatores_energia b_ultimo ON b_ultimo.ano = ultimo.ano " +
            "WHERE e.usuario_id = :usuarioId AND e.ativo = true " +
            "ORDER BY COALESCE(e.ano_referencia, b_ultimo.ano) DESC NULLS LAST", nativeQuery = true)
    List<Object[]> findEnergiaComFatorMedioByUsuarioId(@Param("usuarioId") Long usuarioId);

    // NOVO MÉTODO PARA VERIFICAR DUPLICAÇÃO POR USUÁRIO+ANO+ESCOPO
    Optional<EnergiaECombustivel> findByUsuarioIdAndAnoReferenciaAndEscopoAndAtivoTrue(Long usuarioId,
            Integer anoReferencia, String escopo);

}