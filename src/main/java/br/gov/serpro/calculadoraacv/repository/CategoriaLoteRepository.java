package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.CategoriaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriaLoteRepository extends JpaRepository<CategoriaLote, Long> {
    
    List<CategoriaLote> findByLoteId(Long loteId);
    
    Optional<CategoriaLote> findByIdAndLoteId(Long id, Long loteId);
    
    @Query("SELECT c FROM CategoriaLote c WHERE c.loteId = :loteId AND c.categoriaCorteId = :categoriaCorteId")
    Optional<CategoriaLote> findByLoteIdAndCategoriaCorteId(@Param("loteId") Long loteId, @Param("categoriaCorteId") Long categoriaCorteId);
    
    @Query("SELECT c FROM CategoriaLote c WHERE c.loteId = :loteId AND c.categoriaLeiteId = :categoriaLeiteId")
    Optional<CategoriaLote> findByLoteIdAndCategoriaLeiteId(@Param("loteId") Long loteId, @Param("categoriaLeiteId") Long categoriaLeiteId);
    
    @Query("SELECT COUNT(c) FROM CategoriaLote c WHERE c.loteId = :loteId")
    long countByLoteId(@Param("loteId") Long loteId);
}