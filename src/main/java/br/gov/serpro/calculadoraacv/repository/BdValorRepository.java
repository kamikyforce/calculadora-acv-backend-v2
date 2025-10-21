package br.gov.serpro.calculadoraacv.repository;

import br.gov.serpro.calculadoraacv.model.BdValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdValorRepository extends JpaRepository<BdValor, Long> {
    
    List<BdValor> findByTipo(String tipo);
    
    List<BdValor> findByNomeAlimentoContainingIgnoreCase(String nomeAlimento);
    
    List<BdValor> findByIngredientePadronizacaoContainingIgnoreCase(String ingredientePadronizacao);
    
    @Query("SELECT DISTINCT v.tipo FROM BdValor v WHERE v.tipo IS NOT NULL ORDER BY v.tipo")
    List<String> findDistinctTipo();
    
    @Query("SELECT v FROM BdValor v WHERE v.tipo = :tipo AND (v.nomeAlimento LIKE %:nome% OR v.ingredientePadronizacao LIKE %:nome%) ORDER BY v.nomeAlimento")
    List<BdValor> findByTipoAndNomeContaining(@Param("tipo") String tipo, @Param("nome") String nome);
    
    @Query("SELECT v FROM BdValor v WHERE v.representatividadeCorte = 'Alta' OR v.representatividadeLeite = 'Alta' ORDER BY v.nomeAlimento")
    List<BdValor> findByAltaRepresentatividade();
}