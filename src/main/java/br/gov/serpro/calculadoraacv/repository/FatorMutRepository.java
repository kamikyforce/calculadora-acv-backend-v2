package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.FatorMut;
import br.gov.serpro.calculadoraacv.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FatorMutRepository extends JpaRepository<FatorMut, Long> {

    @Query("SELECT f FROM FatorMut f WHERE f.ativo = true")
    List<FatorMut> findAllAtivos();

    @Query("SELECT f FROM FatorMut f WHERE f.tipoMudanca = :tipo AND f.ativo = true")
    List<FatorMut> findByTipoMudancaAndAtivo(@Param("tipo") TipoMudanca tipo);

    @Query("SELECT f FROM FatorMut f WHERE f.escopo = :escopo AND f.ativo = true")
    List<FatorMut> findByEscopoAndAtivo(@Param("escopo") EscopoEnum escopo);

    // ✅ FIXED: Single fetch query with only vegetacao categories
    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN f.dadosVegetacao dv " +
           "LEFT JOIN dv.categoriasFitofisionomia cf " +
           "WHERE (:tipoMudanca IS NULL OR f.tipoMudanca = :tipoMudanca) " +
           "AND (:escopo IS NULL OR f.escopo = :escopo) " +
           "AND (:ativo IS NULL OR f.ativo = :ativo) " +
           "AND (:bioma IS NULL OR EXISTS (" +
           "    SELECT 1 FROM DadosDesmatamento dd WHERE dd.fatorMut = f AND dd.bioma = :bioma" +
           ") OR EXISTS (" +
           "    SELECT 1 FROM DadosVegetacao dv2 WHERE dv2.fatorMut = f AND dv2.bioma = :bioma" +
           ") OR EXISTS (" +
           "    SELECT 1 FROM DadosSolo ds WHERE ds.fatorMut = f AND ds.bioma = :bioma" +
           ")) " +
           "AND (:nome IS NULL OR " +
           "     f.nome LIKE :nome OR " +
           "     (f.descricao IS NOT NULL AND f.descricao LIKE :nome) OR " +
           "     EXISTS (SELECT 1 FROM DadosDesmatamento dd2 WHERE dd2.fatorMut = f AND dd2.nomeFitofisionomia LIKE :nome) OR " +
           "     EXISTS (SELECT 1 FROM DadosSolo ds2 WHERE ds2.fatorMut = f AND ds2.descricao LIKE :nome))")
    Page<FatorMut> findWithFiltersOnly(
        @Param("tipoMudanca") TipoMudanca tipoMudanca,
        @Param("escopo") EscopoEnum escopo,
        @Param("ativo") Boolean ativo,
        @Param("bioma") Bioma bioma,
        @Param("nome") String nome,
        Pageable pageable
    );

    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN FETCH f.dadosVegetacao dv " +
           "LEFT JOIN FETCH dv.categoriasFitofisionomia " +
           "WHERE f.id IN :ids")
    List<FatorMut> findWithVegetacaoCategories(@Param("ids") List<Long> ids);

    // ✅ NEW: Fetch desmatamento with ufs for specific IDs
    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN FETCH f.dadosDesmatamento dd " +
           "LEFT JOIN FETCH dd.ufs " +
           "WHERE f.id IN :ids")
    List<FatorMut> findWithDesmatamentoAndUfs(@Param("ids") List<Long> ids);

    // ✅ NEW: Fetch dados solo for specific IDs
    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN FETCH f.dadosSolo " +
           "WHERE f.id IN :ids")
    List<FatorMut> findWithDadosSolo(@Param("ids") List<Long> ids);

    @Query("SELECT COUNT(f) FROM FatorMut f WHERE f.ativo = true")
    Long countAtivos();

    @Query("SELECT COUNT(f) FROM FatorMut f WHERE f.ativo = false")
    Long countInativos();

    @Query("SELECT f.tipoMudanca, COUNT(f) FROM FatorMut f WHERE f.ativo = true GROUP BY f.tipoMudanca")
    List<Object[]> countByTipoMudanca();

    @Query("SELECT f.escopo, COUNT(f) FROM FatorMut f WHERE f.ativo = true GROUP BY f.escopo")
    List<Object[]> countByEscopo();

    @Query("SELECT dd.bioma, COUNT(f) FROM FatorMut f " +
           "JOIN f.dadosDesmatamento dd WHERE f.ativo = true GROUP BY dd.bioma")
    List<Object[]> countByBioma();

    boolean existsByTipoMudancaAndEscopoAndAtivoTrue(TipoMudanca tipoMudanca, EscopoEnum escopo);

    boolean existsByTipoMudancaAndEscopoAndAtivoTrueAndIdNot(TipoMudanca tipoMudanca, EscopoEnum escopo, Long id);

    @Query("SELECT f FROM FatorMut f WHERE f.tipoMudanca = :tipoMudanca AND f.escopo = :escopo AND f.ativo = true")
    Optional<FatorMut> findByTipoMudancaAndEscopoAndAtivoTrue(@Param("tipoMudanca") TipoMudanca tipoMudanca, @Param("escopo") EscopoEnum escopo);

    @Query("SELECT f FROM FatorMut f WHERE f.tipoMudanca = :tipoMudanca AND f.escopo = :escopo AND f.ativo = false ORDER BY f.dataAtualizacao DESC")
    List<FatorMut> findByTipoMudancaAndEscopoAndAtivoFalse(@Param("tipoMudanca") TipoMudanca tipoMudanca, @Param("escopo") EscopoEnum escopo);

    @Query("SELECT f FROM FatorMut f WHERE f.id = :id AND f.ativo = true")
    Optional<FatorMut> findByIdAndAtivoTrue(@Param("id") Long id);

    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN FETCH f.dadosVegetacao dv " +
           "LEFT JOIN FETCH dv.categoriasFitofisionomia " +
           "WHERE f.id = :id AND f.ativo = true")
    Optional<FatorMut> findByIdAndAtivoTrueWithVegetacaoCategories(@Param("id") Long id);

    @Query("SELECT DISTINCT f FROM FatorMut f " +
           "LEFT JOIN FETCH f.dadosVegetacao dv " +
           "LEFT JOIN FETCH dv.categoriasFitofisionomia " +
           "WHERE f.tipoMudanca = :tipoMudanca AND f.escopo = :escopo AND f.ativo = true")
    Optional<FatorMut> findByTipoMudancaAndEscopoAndAtivoTrueWithVegetacaoCategories(
        @Param("tipoMudanca") TipoMudanca tipoMudanca, 
        @Param("escopo") EscopoEnum escopo);

    // ✅ UPDATED: Remove problematic multiple fetches
    @Query("SELECT f FROM FatorMut f WHERE f.tipoMudanca = :tipoMudanca AND f.escopo = :escopo AND f.ativo = true ORDER BY f.dataAtualizacao DESC, f.id DESC")
    List<FatorMut> findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(
        @Param("tipoMudanca") TipoMudanca tipoMudanca, 
        @Param("escopo") EscopoEnum escopo
    );
}