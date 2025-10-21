package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.BdDietaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdDietaIngredienteRepository extends JpaRepository<BdDietaIngrediente, Long> {
    
    List<BdDietaIngrediente> findByTipoIngrediente(String tipoIngrediente);
    
    List<BdDietaIngrediente> findByNomeIngredienteContainingIgnoreCase(String nomeIngrediente);
    
    @Query("SELECT DISTINCT d.tipoIngrediente FROM BdDietaIngrediente d ORDER BY d.tipoIngrediente")
    List<String> findDistinctTipoIngrediente();
    
    @Query("SELECT d FROM BdDietaIngrediente d WHERE d.tipoIngrediente = :tipo AND d.nomeIngrediente LIKE %:nome% ORDER BY d.nomeIngrediente")
    List<BdDietaIngrediente> findByTipoAndNomeContaining(@Param("tipo") String tipo, @Param("nome") String nome);
}