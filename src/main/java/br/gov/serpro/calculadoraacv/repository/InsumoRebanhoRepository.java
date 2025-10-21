package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.enums.TipoInsumo;
import br.gov.serpro.calculadoraacv.enums.GrupoIngredienteAlimentar;
import br.gov.serpro.calculadoraacv.enums.FazParteDieta;
import br.gov.serpro.calculadoraacv.enums.EscopoEnum;
import br.gov.serpro.calculadoraacv.model.InsumoRebanho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoRebanhoRepository extends JpaRepository<InsumoRebanho, Long> {
    
    List<InsumoRebanho> findByAtivoTrue();
    
    List<InsumoRebanho> findByUsuarioIdAndAtivoTrue(Long usuarioId);
    
    List<InsumoRebanho> findByTipoAndAtivoTrue(TipoInsumo tipo);
    
    List<InsumoRebanho> findByUsuarioIdAndTipoAndAtivoTrue(Long usuarioId, TipoInsumo tipo);
    
    List<InsumoRebanho> findByEscopoAndAtivoTrue(EscopoEnum escopo);
    
    List<InsumoRebanho> findByUsuarioIdAndEscopoAndAtivoTrue(Long usuarioId, EscopoEnum escopo);
    
    List<InsumoRebanho> findByGrupoIngredienteAndAtivoTrue(GrupoIngredienteAlimentar grupoIngrediente);
    
    List<InsumoRebanho> findByFazParteDietaAndAtivoTrue(FazParteDieta fazParteDieta);
    
    @Query("SELECT i FROM InsumoRebanho i WHERE i.usuarioId = :usuarioId AND i.identificacaoProduto LIKE %:produto% AND i.ativo = true")
    List<InsumoRebanho> findByUsuarioIdAndIdentificacaoProdutoContainingAndAtivoTrue(@Param("usuarioId") Long usuarioId, @Param("produto") String produto);
    
    @Query("SELECT i FROM InsumoRebanho i WHERE i.modulo LIKE %:modulo% AND i.ativo = true")
    List<InsumoRebanho> findByModuloContainingAndAtivoTrue(@Param("modulo") String modulo);
    
    @Query("SELECT DISTINCT i.modulo FROM InsumoRebanho i WHERE i.ativo = true ORDER BY i.modulo")
    List<String> findDistinctModulos();
    
    @Query("SELECT DISTINCT i.escopo FROM InsumoRebanho i WHERE i.ativo = true ORDER BY i.escopo")
    List<EscopoEnum> findDistinctEscopos();
    
    @Query("SELECT DISTINCT i.tipo FROM InsumoRebanho i WHERE i.ativo = true ORDER BY i.tipo")
    List<TipoInsumo> findDistinctTipos();
    
    @Query("SELECT i FROM InsumoRebanho i WHERE " +
           "(:usuarioId IS NULL OR i.usuarioId = :usuarioId) AND " +
           "(:tipo IS NULL OR i.tipo = :tipo) AND " +
           "(:escopo IS NULL OR i.escopo = :escopo) AND " +
           "(:grupoIngrediente IS NULL OR i.grupoIngrediente = :grupoIngrediente) AND " +
           "(:fazParteDieta IS NULL OR i.fazParteDieta = :fazParteDieta) AND " +
           "i.ativo = true")
    List<InsumoRebanho> findWithFilters(@Param("usuarioId") Long usuarioId,
                                       @Param("tipo") TipoInsumo tipo,
                                       @Param("escopo") EscopoEnum escopo,
                                       @Param("grupoIngrediente") GrupoIngredienteAlimentar grupoIngrediente,
                                       @Param("fazParteDieta") FazParteDieta fazParteDieta);
    
    @Query("SELECT COUNT(i) FROM InsumoRebanho i WHERE i.usuarioId = :usuarioId AND i.ativo = true")
    long countByUsuarioIdAndAtivoTrue(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InsumoRebanho i WHERE UPPER(i.identificacaoProduto) = UPPER(:identificacao) AND i.usuarioId = :usuarioId")
    boolean existsByIdentificacaoProdutoAndUsuarioId(@Param("identificacao") String identificacao, @Param("usuarioId") Long usuarioId);
    
    @Query("SELECT DISTINCT i.identificacaoProduto FROM InsumoRebanho i WHERE i.tipo = :tipo AND i.ativo = true ORDER BY i.identificacaoProduto")
    List<String> findDistinctIdentificacaoProdutoByTipoAndAtivoTrue(@Param("tipo") TipoInsumo tipo);
}