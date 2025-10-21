package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.ManejoDejetosLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManejoDejetosLoteRepository extends JpaRepository<ManejoDejetosLote, Long> {
    
    List<ManejoDejetosLote> findByLoteId(Long loteId);
    
    Optional<ManejoDejetosLote> findByIdAndLoteId(Long id, Long loteId);
    
    @Query("SELECT m FROM ManejoDejetosLote m WHERE m.loteId = :loteId AND m.categoriaAnimal = :categoriaAnimal")
    List<ManejoDejetosLote> findByLoteIdAndCategoriaAnimal(@Param("loteId") Long loteId, @Param("categoriaAnimal") String categoriaAnimal);
    
    @Query("SELECT COUNT(m) FROM ManejoDejetosLote m WHERE m.loteId = :loteId")
    long countByLoteId(@Param("loteId") Long loteId);
    
    void deleteByLoteId(Long loteId);

    // Tipos de manejo a partir da tabela de referência bd_manejo_mcf
    @Query(value = "SELECT DISTINCT sistema_manejo FROM bd_manejo_mcf ORDER BY sistema_manejo", nativeQuery = true)
    List<String> listarTiposManejo();
}