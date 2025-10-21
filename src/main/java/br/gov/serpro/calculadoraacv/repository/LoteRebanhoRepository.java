package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.LoteRebanho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoteRebanhoRepository extends JpaRepository<LoteRebanho, Long> {
    
    List<LoteRebanho> findByInventarioIdOrderByOrdem(Long inventarioId);
    
    @Query("SELECT l FROM LoteRebanho l WHERE l.inventarioId = :inventarioId AND l.nome ILIKE %:nome%")
    List<LoteRebanho> findByInventarioIdAndNomeContaining(@Param("inventarioId") Long inventarioId, @Param("nome") String nome);
    
    Optional<LoteRebanho> findByIdAndInventarioId(Long id, Long inventarioId);
    
    @Query("SELECT MAX(l.ordem) FROM LoteRebanho l WHERE l.inventarioId = :inventarioId")
    Integer findMaxOrdemByInventarioId(@Param("inventarioId") Long inventarioId);
    
    @Query("SELECT COUNT(l) FROM LoteRebanho l WHERE l.inventarioId = :inventarioId")
    long countByInventarioId(@Param("inventarioId") Long inventarioId);

    void deleteByInventarioId(Long inventarioId);
}